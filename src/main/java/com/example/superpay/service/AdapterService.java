package com.example.superpay.service;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.*;
import com.example.superpay.entity.*;
import com.example.superpay.repository.OrderRepository;
import com.example.superpay.repository.PayTypeRepository;
import com.example.superpay.repository.ThirdPartyRepository;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.AlipayUtil;
import com.example.superpay.util.ThirdPartyUtil;
import com.example.superpay.util.TimeUtil;
import com.example.superpay.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
public class AdapterService {
    private static int PAY_TYPE_INDEX = 0;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PayTypeRepository payTypeRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    public ModelAndView ePayOrder(EPayData ePay) {
//        System.out.printf(JSONObject.toJSONString(ePay));
        if (ePay.getPid() == 0) {
            return ToolsUtil.errorHtml("商户ID不为空!");
        }
        User user = userRepository.findAllByPid(ePay.getPid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户ID不存在!");
        }
        if (user.getState() != 0) {
            return ToolsUtil.errorHtml("商户存在异常，请联系管理员!");
        }
        if (!ePay.isSign(user.getSecretKey())) {
            return ToolsUtil.errorHtml("数据效验失败!");
        }
        Order order = orderRepository.findAllByOutTradeNo(ePay.getOut_trade_no());
        if (order != null) {
            if (order.getState() == 1) {
                return ToolsUtil.errorHtml("商户订单已重复!");
            } else {
                return ToolsUtil.getHtml("/v3api/callback?outTradeNo=" + order.getOutTradeNo());
            }
        }
        PayType type = payTypeRepository.findAllByType(ePay.getType());
        if (type == null) {
            return ToolsUtil.errorHtml("支付方式不存在或者未开放!");
        }
        ThirdParty party = getThird(type.getId());
        if (party == null) {
            return ToolsUtil.errorHtml("通道已关闭！");
        }
//        Order order = new Order();
        order = new Order();
        order.setUid(user.getId());
        order.setOutTradeNo(ePay.getOut_trade_no());
        order.setThirdPartyId(party.getId());
        order.setIp(ePay.getIp());

        if (StringUtils.isNotEmpty(ePay.getReturn_url())) {
            order.setReturnUrl(ePay.getReturn_url());
        } else if (StringUtils.isNotEmpty(user.getCallbackUrl())) {
            order.setReturnUrl(user.getCallbackUrl());
        } else {
            return ToolsUtil.errorHtml("同步通知地址不可为空！");
        }
        if (StringUtils.isNotEmpty(ePay.getNotify_url())) {
            order.setNotifyUrl(ePay.getNotify_url());
        } else if (StringUtils.isNotEmpty(user.getNotifyUrl())) {
            order.setNotifyUrl(user.getNotifyUrl());
        } else {
            return ToolsUtil.errorHtml("异步通知地址不可为空！");
        }
        if (!order.getReturnUrl().startsWith("http") || !order.getNotifyUrl().startsWith("http"))
            return ToolsUtil.errorHtml("通知回调地址不可为空！");
        order.setMoney(new BigDecimal(ePay.getMoney()).doubleValue());
//        order.setName(ePay.getName());
        order.setAddTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());

        return handlerThirdParty(party, order, ePay.getType());
    }

    private ModelAndView handlerThirdParty(ThirdParty thirdParty, Order order, String type) {
        switch (thirdParty.getThird()) {
            case 0:
                String url = ThirdPartyUtil.toPay(order, thirdParty);
                if (url == null) {
                    return ToolsUtil.errorHtml("未获取到第三方资源！");
                }
                orderRepository.save(order);
                return ToolsUtil.getHtml(url);
            case 1:
                if (type.equals("wxpay")){}else if (type.equals("alipay")){
                    String data = AlipayUtil.alipay(thirdParty,order);
                    if (data == null) {
                        return ToolsUtil.errorHtml("未获取到第三方资源！");
                    }
                    orderRepository.save(order);
                    return ToolsUtil.emptyHtml(data);
                }
        }
        return null;
    }

    private ThirdParty getThird(String type) {
        Pageable pageable = PageRequest.of(PAY_TYPE_INDEX, 1);
        Page<ThirdParty> parties = thirdPartyRepository.findAllByTypeIdAndState(type, 1, pageable);
        if (parties.getTotalPages() < PAY_TYPE_INDEX || parties.getTotalPages() == PAY_TYPE_INDEX) {
            PAY_TYPE_INDEX = 0;
            pageable = PageRequest.of(PAY_TYPE_INDEX, 1);
            parties = thirdPartyRepository.findAllByTypeIdAndState(type, 1, pageable);
        }
        if (parties.getTotalPages() > PAY_TYPE_INDEX) {
            PAY_TYPE_INDEX++;
        }
        if (parties.getTotalPages() < PAY_TYPE_INDEX || parties.getTotalPages() == PAY_TYPE_INDEX) {
            PAY_TYPE_INDEX = 0;
        }
        if (parties.getContent().size() == 0) return null;
        return parties.getContent().get(0);
    }

    public EPayData test() {
//        ThirdParty thirdParty = new ThirdParty();
//        thirdParty.setThird(1);
//        thirdParty.setAddTime(System.currentTimeMillis());
//        thirdParty.setState(1);
//        thirdParty.setTitle("支付宝原生");
//        thirdParty.setMchId("2018031602385705");
//        thirdParty.setSecretKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvTpNi61GjIhFrdRaPPyIlhHeZ3TPUkGvjoPKb2Yptwe8uy5YS8p6jnTohSJp0aIvA8SduBCZNfgzNq32bOXwWrdar3tPNQj0t62A7ucwFhxoMFMtnbzOjCIEKZWWC3L3J6WaNstchl35DL0RupVWOYJ5j27MfHch4IoLzanZxpRRzuLE3YMsvSL5tNES6rNqlPSfiAiPvLV7mWLByWDJJVW5+A5oFlfeFD2Ey0tzlEFslPvvVKhP0thByyEqn2d8//6rkieKjEM/BsFAXjl40RUWeUCRorS7o1TSZC9Hn9azjvOFF2sgOrsd5HG4Lhh9WxvblZ1AshwZxeNe/ZustAgMBAAECggEADlksDwibofKD4nuu4QKV1ORGtb05JMi9S+A8ey0O+3TIEthu7BYXjeSsgVTj72svJReX1pVYXTdX7O2AVlgaI/EOhPqz8zTctQly0vCeFkW8iAibrVeYrltf1G4AJPnUPtZvomFk4kb3+p+/xh6aJhEaZanxuzZA1jRc63dnQl3RjnvjSqcE7deawK096rMlBr82XoEWltoN6xpUuLqoJ/nwqS5EXd54fEDc4sTZgGHW1A5WOp/t1+UVfoG4C+0pl1uiC5eALl1rlzdx23IOEU5V4FAwTDNfb442B/i+U/FglQwQw62yPkz4J5e9xGuEVbvtPZmvtInbYRnXaIxFAQKBgQDzkb8AQ02ZK5+QE5no0M0YOrBB8DnFekTeuy7OjuhKml2NjVVNXDkEd+z6/2n/RfSBiFsgEOPMizRgAF0esByuESzjnvsXZ2BFZRlQ7Ni7+U6aPXnY8gdnkIvS/5V4F83yBqLBz3eC+GPajnUXSZwc1z1bnhx1m15rRNFX6cqBfQKBgQC4QPnMIZcg+54UkJVFlK5ULOCmrILUDiwLFxth1nxFe1MZfTxypQcfNexRyrZb8KfRsgZNf6M53dE6rgSJDK46tAPF+O6jPtvILwKdSXLvxGTb7pQ2ovPgBbz+CQH7SKDthypDXUMbR5VCmtyJJeK4LW/iOKTluV1MkomxvJ4/cQKBgER6EEHJqjJK4mRGLnoW4eJS9aTEHenYEy6vX1xxLvtyZKTcPEQwjlMkSDrUvf8nsrMMG9prBTBHXqUy1PtAtf92ErG3y43r4VQBNVncDJ7kW2XfrLcCbHSAXd8nPeVyg9LsbKuiYU4v+RrD/EVcy4gMN1Lfo86orKXpxhU6RFWdAoGAXDiZTqSZYfbOfniHXhY20wbLQmEh8kVNohdkqymRda1uQFnAgZk74VE6AQ43C/l95aT3Jp718aambHpg5r+kDNnA8bvQpYB2vNFau6LhlkR0PuhA4r/Y1I3KtFOJ3F3Tvk9ixejOB79iY73jF/oQaiLD1zSGxDxtCEBoDr/bbOECgYBQHaza5KpWbu1dNSfXe0hb15vWaRlawEWogqoBOwmcGe06wXnuy6IC26qKVo6oCWFABzHjMtNADxwEpTqxwoFqrRBHtPvc9fc7L3F1ARfa1Wq6lZBzdMEZ4ZfCyhFcFg5C+HrLtwjOH1o3GCdTtRG+WK8IstFPxK00pG3bBG7/ZA==");
//        thirdParty.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0h5BDdwC3b4rLJDuUwf7pRat3UT9EaJJUh41jYTiqKKsHHBWUiJo/znjqYRn9A5Lx76XtLXXyfEM4DxffqEzu1GTDb3BMc/mZDNXe94xqqAzvC2waMEgP7gzBW/SSNjV0cTLpRLuL34kvgG4mHcGQX9ea9Ux6Oo5jdzr7Da4bccNH2abznrzgB2w+qY21UHHt27JgA0XaRecTrSYABHf95dOWPIKyId03IDWJe58N51/jFm8Ku5r0KFZg4VdU/Cv2HVm2QEnmUyQJbLVo+yVJCBeTbT9epKakq+20LUNMmv+PwWyW8qw7XgP+8NlX7VLHlsbsJY2jKcfXpmeEBsRFQIDAQAB");
//        thirdParty.setNotifyUrl("https://pay.telebott.com/v3api/alipayNotify");
//        thirdParty.setCallbackUrl("https://pay.telebott.com/v3api/alipay");
//        thirdParty.setTypeId("de12ad07CsHdf1ekeu4965KrRb08ec9F89088fa05746ciI");
//        thirdParty.setRate(0.1);
//        thirdPartyRepository.save(thirdParty);
//        PayType type = new PayType();
//        type.setType("alipay");
//        type.setAddTime(System.currentTimeMillis());
//        type.setUpdateTime(System.currentTimeMillis());
//        type.setName("支付宝");
//        payTypeRepository.save(type);
        EPayData data = new EPayData();
        data.setPid(202206252104000L);
        data.setType("alipay");
        data.setOut_trade_no(TimeUtil._getOrderNo());
        data.setNotify_url("https://pay.telebott.com/v3api/ePayNotify");
        data.setReturn_url("https://pay.telebott.com/v3api/ePayReturn");
        data.setMoney("1.00");
        data.setSign(data.getSign("3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk"));
        return data;
    }

    public ModelAndView sError() {
        return ToolsUtil.errorHtml("支付失败");
    }

    public ModelAndView sCallback(String out_trade_no, String ip) {
        System.out.printf("test out_trade_no:" + out_trade_no + " ip:" + ip + "\n");
        Order order = orderRepository.findAllByOutTradeNo(out_trade_no);
        if (order == null) {
            return ToolsUtil.errorHtml("订单号不存在!");
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户不存在!");
        }
        if (order.getState() == 0) {
            return ToolsUtil.waitHtml();
        }
        String url = order.getReturnUrl();
        EPayNotify notify = getEPayNotify(order, user);
        try {
            URL u = new URL(url);
            if (StringUtils.isNotEmpty(u.getQuery())) {
                url = url + "&";
            } else {
                url = url + "?";
            }
            return ToolsUtil.getHtml(url + EPayNotify.getParameter(notify));
        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return ToolsUtil.errorHtml("商户回调地址错误!");
    }

    public String sNotify(ToPayNotify toPayNotify) {
        System.out.printf("toPayNotify:", toPayNotify + "\n");
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(toPayNotify.getMchid());
        if (partys.isEmpty()) {
            return "error";
        }
        ThirdParty party = partys.get(0);
        if (!ThirdPartyUtil.toPayVerify(toPayNotify, party)) {
            return "error";
        }
        Order order = orderRepository.findAllByOutTradeNo(toPayNotify.getOut_trade_no());
        if (order == null) {
            return "error";
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return "error";
        }
        if (order.getState() == 1) {
            return "success";
        }
        order.setUpdateTime(System.currentTimeMillis());
        order.setState(1);
        order.setNotifyState(0);
        order.setTradeNo(toPayNotify.getTrade_no());
        order.setTotalFee(new BigDecimal(toPayNotify.getTotal_fee()).doubleValue());
        double fee = 0D;
        Fee f = new Fee();
        f.setAddTime(System.currentTimeMillis());
        f.setUid(user.getId());
        f.setOrderId(order.getId());
        f.setTip("第三方通道手续费");
        /**
         * 扣除通道费
         */
        if (party.getRate() > 0) {
            Double rate = party.getRate();
            Double rateFee = 0D;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                } else {
                    rateFee = order.getTotalFee();
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
            }
            fee = ToolsUtil.getMoney(rateFee);
            f.setPartyFee(rateFee);
        }
        /**
         * 扣除自定义费率
         */
        if (user.getRate() > 0) {
            Double rate = user.getRate();
            Double rateFee = 0D;
            Double totalFee = order.getTotalFee() - fee;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                } else {
                    rateFee = order.getTotalFee();
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                if (rateFee > totalFee) {
                    rateFee = totalFee;
                }
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setRateFee(ToolsUtil.getMoney(rateFee));
        }
        /**
         * 扣除单笔手续费
         */
        if (user.getFee() > 0) {
            Double totalFee = order.getTotalFee() - fee;
            Double rateFee = 0D;
            if (user.getFee() > totalFee) {
                rateFee = totalFee;
            } else {
                rateFee = user.getFee();
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setSelfFee(ToolsUtil.getMoney(rateFee));
        }
        order.setFee(fee);
        orderRepository.save(order);
        if (f.isNotEmpty()) {
            mongoTemplate.save(f);
        }
        handlerThirdPartyNotify(order);
        return "success";
    }

    private String getPayType(String thirdId) {
        ThirdParty thirdParty = thirdPartyRepository.findAllById(thirdId);
        if (thirdParty != null) {
            PayType type = payTypeRepository.findAllById(thirdParty.getTypeId());
            if (type != null) {
                return type.getType();
            }
        }
        return "unknown";
    }

    public EPayNotify getEPayNotify(Order order, User user) {

        EPayNotify notify = new EPayNotify();
        notify.setPid(user.getPid());
        notify.setTrade_no(order.getTradeNo());
        notify.setOut_trade_no(order.getOutTradeNo());
        notify.setType(getPayType(order.getThirdPartyId()));
        notify.setMoney(String.valueOf(order.getMoney()));
        notify.setTrade_status(order.getState() == 1 ? "TRADE_SUCCESS" : "TRADE_FAILED");
        if (StringUtils.isNotEmpty(order.getName())) {
            notify.setName(order.getName());
        }
        notify.setSign(notify.getSign(user.getSecretKey()));
//        System.out.printf(notify+"\n");
        return notify;
    }

    private void handlerThirdPartyNotify(Order order) {
        User user = userRepository.findAllById(order.getUid());
        if (user == null) return;
        String url = order.getNotifyUrl();
        EPayNotify notify = getEPayNotify(order, user);
        if (ToolsUtil.request(url, EPayNotify.getParameter(notify)).equals("success")) {
            order.setNotifyState(1);
        }
        orderRepository.save(order);
    }

    public String ePayNotify(EPayNotify ePayNotify) {
        System.out.printf("%s\n", ePayNotify);
        if (ePayNotify.getPid() == 0) return "error";
        User user = userRepository.findAllByPid(ePayNotify.getPid());
        boolean verify = ePayNotify.isSign(user.getSecretKey());
        System.out.printf(verify ? "效验成功！\n" : "效验失败!\n");
        if (verify) {
            return "success";
        }
        return "error";
    }

    public ModelAndView ePayReturn(EPayNotify ePayNotify) {
        if (ePayNotify.getPid() == 0) return ToolsUtil.errorHtml("商户不存在!");
        User user = userRepository.findAllByPid(ePayNotify.getPid());
        return ToolsUtil.errorHtml(ePayNotify.isSign(user.getSecretKey()) ? "效验成功！" : "效验失败!");
    }

    public String alipayNotify(AlipayNotifyParam param, HttpServletRequest req) {
        if (StringUtils.isEmpty(param.getApp_id())) return "fail";


        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(param.getApp_id());
        if (partys.isEmpty()) {
            return "error";
        }
        ThirdParty party = partys.get(0);
        if(!AlipayUtil.checkSign(req, party)) return "error";
        Order order = orderRepository.findAllByOutTradeNo(param.getOut_trade_no());
        if (order == null) {
            return "error";
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return "error";
        }
        if (order.getState() == 1) {
            return "success";
        }
        order.setUpdateTime(System.currentTimeMillis());
        order.setState(1);
        order.setNotifyState(0);
        order.setTradeNo(param.getTrade_no());
        order.setTotalFee(new BigDecimal(String.valueOf(param.getTotal_amount())).doubleValue());
        double fee = 0D;
        Fee f = new Fee();
        f.setAddTime(System.currentTimeMillis());
        f.setUid(user.getId());
        f.setOrderId(order.getId());
        f.setTip("第三方通道手续费");
        /**
         * 扣除通道费
         */
        if (party.getRate() > 0) {
            Double rate = party.getRate();
            Double rateFee = 0D;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                } else {
                    rateFee = order.getTotalFee();
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
            }
            fee = ToolsUtil.getMoney(rateFee);
            f.setPartyFee(rateFee);
        }
        /**
         * 扣除自定义费率
         */
        if (user.getRate() > 0) {
            Double rate = user.getRate();
            Double rateFee = 0D;
            Double totalFee = order.getTotalFee() - fee;
            if (rate > 1) {
                if (rate < 100) {
                    rateFee = ToolsUtil.getMoney(order.getTotalFee() * (rate / 100));
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                } else {
                    rateFee = order.getTotalFee();
                    if (rateFee > totalFee) {
                        rateFee = totalFee;
                    }
                }
            } else {
                rateFee = ToolsUtil.getMoney(order.getTotalFee() * rate);
                if (rateFee > totalFee) {
                    rateFee = totalFee;
                }
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setRateFee(ToolsUtil.getMoney(rateFee));
        }
        /**
         * 扣除单笔手续费
         */
        if (user.getFee() > 0) {
            Double totalFee = order.getTotalFee() - fee;
            Double rateFee = 0D;
            if (user.getFee() > totalFee) {
                rateFee = totalFee;
            } else {
                rateFee = user.getFee();
            }
            fee = fee + ToolsUtil.getMoney(rateFee);
            f.setSelfFee(ToolsUtil.getMoney(rateFee));
        }
        order.setFee(fee);
        orderRepository.save(order);
        if (f.isNotEmpty()) {
            mongoTemplate.save(f);
        }
        handlerThirdPartyNotify(order);
        return "success";
    }

    public ModelAndView alipay(AlipayNotifyParam param, HttpServletRequest req) {
        if (StringUtils.isEmpty(param.getApp_id())) return ToolsUtil.errorHtml("参数错误");
        List<ThirdParty> partys = thirdPartyRepository.findAllByMchId(param.getApp_id());
        if(partys.size() == 0) return ToolsUtil.errorHtml("商户不存在！");
        ThirdParty thirdParty = partys.get(0);
        if (!AlipayUtil.checkSign(req, thirdParty)) return ToolsUtil.errorHtml("效验失败");
        Order order = orderRepository.findAllByOutTradeNo(param.getOut_trade_no());
        if (order == null) {
            return ToolsUtil.errorHtml("订单号不存在!");
        }
        User user = userRepository.findAllById(order.getUid());
        if (user == null) {
            return ToolsUtil.errorHtml("商户不存在!");
        }
        if (order.getState() == 0) {
            return ToolsUtil.waitHtml();
        }
        String url = order.getReturnUrl();
        EPayNotify notify = getEPayNotify(order, user);
        try {
            URL u = new URL(url);
            if (StringUtils.isNotEmpty(u.getQuery())) {
                url = url + "&";
            } else {
                url = url + "?";
            }
            return ToolsUtil.getHtml(url + EPayNotify.getParameter(notify));
        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return ToolsUtil.errorHtml("商户回调地址错误!");
    }
}
