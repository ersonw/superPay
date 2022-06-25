package com.example.superpay.service;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.*;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.PayType;
import com.example.superpay.entity.ThirdParty;
import com.example.superpay.entity.User;
import com.example.superpay.repository.OrderRepository;
import com.example.superpay.repository.PayTypeRepository;
import com.example.superpay.repository.ThirdPartyRepository;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.ThirdPartyUtil;
import com.example.superpay.util.TimeUtil;
import com.example.superpay.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdapterService {
    private static int PAY_TYPE_INDEX = 0;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PayTypeRepository payTypeRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    public ModelAndView ePayOrder(EPayData ePay) {
        if (ePay.getPid() == 0) {
            return ToolsUtil.errorHtml("商户ID不为空!");
        }
        User user = userRepository.findAllByPid(ePay.getPid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户ID不存在!");
        }
        if(!ePay.isSign(user.getSecretKey())){
            return ToolsUtil.errorHtml("数据效验失败!");
        }
        if(orderRepository.findAllByOutTradeNo(ePay.getOut_trade_no()) != null){
            return ToolsUtil.errorHtml("商户订单已重复!");
        }
        PayType type = payTypeRepository.findAllByType(ePay.getType());
        if(type == null){
            return ToolsUtil.errorHtml("支付方式不存在或者未开放!");
        }
        ThirdParty party = getThird(type.getId());
        if(party == null){
            return ToolsUtil.errorHtml("通道已关闭！");
        }
        Order order = new Order();
        order.setUid(user.getId());
        order.setOutTradeNo(ePay.getOut_trade_no());
        order.setThirdPartyId(party.getId());
        order.setIp(ePay.getIp());

        if(StringUtils.isNotEmpty(ePay.getReturn_url())){
            order.setReturnUrl(ePay.getReturn_url());
        }else if(StringUtils.isNotEmpty(user.getCallbackUrl())){
            order.setReturnUrl(user.getCallbackUrl());
        }else {
            return ToolsUtil.errorHtml("同步通知地址不可为空！");
        }
        if(StringUtils.isNotEmpty(ePay.getNotify_url())){
            order.setNotifyUrl(ePay.getNotify_url());
        }else if(StringUtils.isNotEmpty(user.getNotifyUrl())){
            order.setNotifyUrl(user.getNotifyUrl());
        }else {
            return ToolsUtil.errorHtml("异步通知地址不可为空！");
        }

        order.setMoney(ePay.getMoney());
        order.setName(ePay.getName());
        order.setAddTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());

        String url = handlerThirdParty(party,order);
        if(url == null){
            return ToolsUtil.errorHtml("未获取到第三方资源！");
        }
        orderRepository.save(order);
        return ToolsUtil.postHtml(url);
    }
    private String handlerThirdParty(ThirdParty thirdParty, Order order){
        switch (thirdParty.getThird()){
            case 0:
                return ThirdPartyUtil.toPay(order,thirdParty);
        }
        return null;
    }
    private ThirdParty getThird(String type){
        Pageable pageable = PageRequest.of(PAY_TYPE_INDEX, 1);
        Page<ThirdParty> parties = thirdPartyRepository.findAllByTypeIdAndState(type,1,pageable);
        if(parties.getTotalPages() < PAY_TYPE_INDEX || parties.getTotalPages() == PAY_TYPE_INDEX){
            PAY_TYPE_INDEX = 0;
            pageable = PageRequest.of(PAY_TYPE_INDEX, 1);
            parties = thirdPartyRepository.findAllByTypeIdAndState(type,1,pageable);
        }
        if(parties.getTotalPages() > PAY_TYPE_INDEX){
            PAY_TYPE_INDEX++;
        }
        if(parties.getTotalPages() < PAY_TYPE_INDEX || parties.getTotalPages() == PAY_TYPE_INDEX){
            PAY_TYPE_INDEX = 0;
        }
        if(parties.getContent().size() ==0) return null;
        return parties.getContent().get(0);
    }

    public EPayData test() {
        EPayData data = new EPayData();
        data.setPid(202206252104000L);
        data.setType("WechatPay");
        data.setOut_trade_no(TimeUtil._getOrderNo());
        data.setNotify_url("https://pay.telebott.com/v3api/ePayNotify");
        data.setReturn_url("https://pay.telebott.com/v3api/ePayReturn");
        data.setMoney("1.00");
        data.setSign(data.getSign("3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk"));
        return data;
//        System.out.printf(data.getSign("3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk")+"\n");
//        System.out.printf( data.getOut_trade_no()+"\n");

//        PayType payType = new PayType();
//        payType.setType("WechatPay");
//        payType.setName("微信支付");
//        payType.setAddTime(System.currentTimeMillis());
//        payType.setUpdateTime(System.currentTimeMillis());
////        payTypeRepository.save(payType);
//
//        ThirdParty thirdParty = new ThirdParty();
//        thirdParty.setDomain("http://121.199.22.139:8211/toPay");
//        thirdParty.setMchId("2c9180838153974501815f66c8540bb8");
//        thirdParty.setSecretKey("eeae24639e4146479ca075fdb46a13cf");
//        thirdParty.setTitle("自三方");
//        thirdParty.setTypeId(payType.getId());
//        thirdParty.setCallbackUrl("https://pay.telebott.com/v3api/callback");
//        thirdParty.setNotifyUrl("https://pay.telebott.com/v3api/error");
//        thirdParty.setErrorUrl("https://pay.telebott.com/v3api/notify");
//        thirdParty.setState(1);
//        thirdParty.setAddTime(System.currentTimeMillis());
//        thirdParty.setUpdateTime(System.currentTimeMillis());
////        thirdPartyRepository.save(thirdParty);

    }

    public ModelAndView sError() {
        return ToolsUtil.errorHtml("支付失败");
    }

    public ModelAndView sCallback(String out_trade_no, String ip) {
        System.out.printf("test out_trade_no:"+out_trade_no + "\n ip:"+ip);
        Order order = orderRepository.findAllByOutTradeNo(out_trade_no);
        if(order == null){
            return ToolsUtil.errorHtml("订单号不存在!");
        }
        User user = userRepository.findAllById(order.getUid());
        if(user == null) {
            return ToolsUtil.errorHtml("商户不存在!");
        }
        if(order.getState() == 0){
            return ToolsUtil.waitHtml();
        }
        String url = user.getCallbackUrl();
        if(StringUtils.isNotEmpty(order.getReturnUrl())){
            url = order.getReturnUrl();
        }
        EPayNotify notify = getEPayNotify(order,user);
//        System.out.printf(notify+"\n");
        return ToolsUtil.postHtml(url+"?"+ EPayNotify.getParameter(notify));
    }

    public String sNotify(ToPayNotify toPayNotify) {
        System.out.printf("toPayNotify:",toPayNotify + "\n");
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(toPayNotify.getMchid());
        if (partys.isEmpty()){
            return "error";
        }
        if(!ThirdPartyUtil.toPayVerify(toPayNotify, partys.get(0))){
            return "error";
        }
        Order order = orderRepository.findAllByOutTradeNo(toPayNotify.getOut_trade_no());
        if(order == null){
            return "error";
        }
        if(order.getState() == 1){
            return "success";
        }
        order.setState(1);
        order.setNotifyState(0);
        order.setTradeNo(toPayNotify.getTrade_no());
        order.setTotalFee(toPayNotify.getTotal_fee());
        orderRepository.save(order);
        handlerThirdPartyNotify(order);
        return "success";
    }
    private String getPayType(String thirdId){
        ThirdParty thirdParty = thirdPartyRepository.findAllById(thirdId);
        if(thirdParty != null){
            PayType type = payTypeRepository.findAllById(thirdParty.getTypeId());
            if(type != null){
                return type.getType();
            }
        }
        return "unknown";
    }
    private EPayNotify getEPayNotify(Order order,User user){

        EPayNotify notify = new EPayNotify();
        notify.setPid(user.getPid());
        notify.setTrade_no(order.getTradeNo());
        notify.setOut_trade_no(order.getOutTradeNo());
        notify.setType(getPayType(order.getThirdPartyId()));
        notify.setMoney(order.getTotalFee());
        notify.setTrade_status("TRADE_SUCCESS");
        if(StringUtils.isNotEmpty(order.getName())){
            notify.setName(order.getName());
        }
        notify.setSign(notify.getSign(user.getSecretKey()));
//        System.out.printf(notify+"\n");
        return notify;
    }
    private void handlerThirdPartyNotify(Order order) {
        User user = userRepository.findAllById(order.getUid());
        if(user == null) return;
        String url = user.getNotifyUrl();
        if(StringUtils.isNotEmpty(order.getNotifyUrl())){
            url = order.getNotifyUrl();
        }
        EPayNotify notify = getEPayNotify(order,user);
        if(ToolsUtil.request(url, EPayNotify.getParameter(notify)).equals("success")){
            order.setNotifyState(1);
        }
        orderRepository.save(order);
    }

    public String ePayNotify(EPayNotify ePayNotify) {
        if(ePayNotify.getPid() == 0) return "error";
        User user = userRepository.findAllByPid(ePayNotify.getPid());
        boolean verify = ePayNotify.isSign(user.getSecretKey());
        System.out.printf(verify ? "效验成功！": "效验失败!");
        if(verify){
            return "success";
        }
        return "error";
    }

    public ModelAndView ePayReturn(EPayNotify ePayNotify) {
        if(ePayNotify.getPid() == 0) return ToolsUtil.errorHtml("商户不存在!");
        User user = userRepository.findAllByPid(ePayNotify.getPid());
        return ToolsUtil.errorHtml(ePayNotify.isSign(user.getSecretKey()) ? "效验成功！": "效验失败!");
    }
}
