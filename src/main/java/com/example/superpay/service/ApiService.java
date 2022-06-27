package com.example.superpay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.superpay.dao.AdminDao;
import com.example.superpay.dao.OrderDao;
import com.example.superpay.dao.WithDrawDao;
import com.example.superpay.data.EPayNotify;
import com.example.superpay.data.ResponseData;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.PayType;
import com.example.superpay.entity.ThirdParty;
import com.example.superpay.entity.User;
import com.example.superpay.repository.*;
import com.example.superpay.util.JSONUtil;
import com.example.superpay.util.TimeUtil;
import com.example.superpay.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ApiService {
    @Autowired
    private AdapterService adapterService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private PayTypeRepository payTypeRepository;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WithDrawDao withDrawDao;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private LoginRepository loginRepository;

    public ResponseData dashboard(User user, String ip) {
//        System.out.printf("isIpAddress:%s\n",adminDao.isIpAddress(user.getId(), ip));
//        Pageable pageable = PageRequest.of(0, 1);
//        Page<Order> orderPage = orderRepository.findAllByAddTimeGreaterThanEqual(1656241208239L,pageable);
//        System.out.printf(orderPage.getTotalElements()+"\n");
        if (user == null || StringUtils.isEmpty(user.getId())) return ResponseData.error();
        JSONArray array = new JSONArray();
        /**
         * 获取今日 昨日收款
         */
        Double tMoney = orderDao.getOrderSum(user.getId(),TimeUtil.getTodayZero(),TimeUtil.getAfterDaysZero(1));
        Double yMoney = orderDao.getOrderSum(user.getId(),TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero());
        if (user.isAdmin()){
            tMoney = orderDao.getOrderSum(TimeUtil.getTodayZero(),TimeUtil.getAfterDaysZero(1));
            yMoney = orderDao.getOrderSum(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero());
        }
        JSONObject object = ResponseData.object("title","今日收款");
        object.put("context", tMoney+"元");
        object.put("bottomLeft","昨日收款");
        object.put("bottomRight",yMoney+"元");
        object.put("percentage",new Double(tMoney / yMoney * 100).longValue());
        array.add(object);
        /**
         * 获取今日 昨日订单数
         */
        Long tOrders = orderDao.getOrderCount(user.getId(),TimeUtil.getTodayZero(),TimeUtil.getAfterDaysZero(1));
        Long yOrders = orderDao.getOrderCount(user.getId(),TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero());
        if (user.isAdmin()){
            tOrders = orderDao.getOrderCount(TimeUtil.getTodayZero(),TimeUtil.getAfterDaysZero(1));
            yOrders = orderDao.getOrderCount(TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero());
        }
        object = ResponseData.object("title","今日订单");
        object.put("context", tOrders.toString());
        object.put("bottomLeft","昨日订单");
        object.put("bottomRight",yOrders.toString());
        object.put("percentage",new Double(tOrders.doubleValue() / yOrders.doubleValue() * 100).longValue());
        array.add(object);
        /**
         * 获取总 收款金额 订单量
         */
        Double aMoney = orderDao.getOrderSum(user.getId());
        Long aOrders = orderDao.getOrderCount(user.getId());
        Long sOrders = orderDao.getOrderCount(user.getId(),1);
        if (user.isAdmin()){
            aOrders = orderDao.getOrderCount();
            sOrders = orderDao.getOrderCount(1);
        }
        object = ResponseData.object("title","总收款");
        object.put("context", aMoney+"元");
        object.put("bottomLeft","总订单量");
        object.put("bottomRight",aOrders.toString());
        object.put("percentage",new Double(sOrders.doubleValue() / aOrders.doubleValue() * 100).longValue());
        array.add(object);
        /**
         * 获取 可提现金额 已提现金额
         */
        Double aTotalFee = orderDao.getAllMoney(user.getId());
        Double aWithDraw = withDrawDao.getWithDrawSum(user.getId(),1);
        Double withDraw = withDrawDao.getWithDrawSum(user.getId());
        if (user.isAdmin()){
            aTotalFee = orderDao.getAllMoney();
            aWithDraw = withDrawDao.getWithDrawSum(1);
            withDraw = withDrawDao.getWithDrawSum();
        }
        object = ResponseData.object("title","可提现");
        object.put("context", String.format("%.2f",aTotalFee-withDraw)+"元");
        object.put("bottomLeft","已提现");
        object.put("bottomRight",String.format("%.2f",aWithDraw)+"元");
        object.put("percentage",new Double(withDraw / aTotalFee * 100).longValue());
        array.add(object);
        return ResponseData.success(array);
    }

    public ResponseData dayData(User user, String ip) {
        if (user == null || StringUtils.isEmpty(user.getId())) return ResponseData.error();
        long tTime = TimeUtil.getTodayZero();
        long aTime = TimeUtil.getAfterDaysZero(1);
        long yTime = TimeUtil.getBeforeDaysZero(1);
        /**
         * 获取总流水
         */
        Double tWater = orderDao.getOrderSum(user.getId(),tTime,aTime);
        Double yWater = orderDao.getOrderSum(user.getId(),yTime,tTime);
        if (user.isAdmin()){
            tWater = orderDao.getOrderAdminSum(tTime,aTime);
            yWater = orderDao.getOrderAdminSum(yTime,tTime);
        }
        /**
         * 获取总订单
         */
        Long tOrders = orderDao.getOrderCount(user.getId(),tTime,aTime);
        Long yOrders = orderDao.getOrderCount(user.getId(),yTime,tTime);
        if (user.isAdmin()){
             tOrders = orderDao.getOrderAdminCount(tTime,aTime);
             yOrders = orderDao.getOrderAdminCount(yTime,tTime);
        }
        /**
         * 获取总成功量
         */
        Long tSuccess = orderDao.getOrderCount(user.getId(),tTime,aTime,1);
        Long ySuccess = orderDao.getOrderCount(user.getId(),yTime,tTime,1);
        if (user.isAdmin()){
            tSuccess = orderDao.getOrderCount(tTime,aTime,1);
            ySuccess = orderDao.getOrderCount(yTime,tTime,1);
        }
        long lTime = TimeUtil.getMinutes(-1);
        long nTime = System.currentTimeMillis() - (60*1000L);
        long nowTime = TimeUtil.getMinutes(0);

        /**
         * 获取上一分钟
         */
        Long lOrders = orderDao.getOrderCount(user.getId(),lTime,nowTime);
        Long lSuccess = orderDao.getOrderCount(user.getId(),lTime,nowTime,1);
        Long lYOrders = orderDao.getOrderCount(user.getId(),lTime-86400000L,nowTime-86400000L);
        Long lYSuccess = orderDao.getOrderCount(user.getId(),lTime-86400000L,nowTime-86400000L,1);
        if (user.isAdmin()){
            lOrders = orderDao.getOrderCount(lTime,nowTime);
            lSuccess = orderDao.getOrderCount(lTime,nowTime,1);
            lYOrders = orderDao.getOrderCount(lTime-86400000L,nowTime-86400000L);
            lYSuccess = orderDao.getOrderCount(lTime-86400000L,nowTime-86400000L,1);
        }
        /**
         * 获取近一分钟
         */
        Long nOrders = orderDao.getOrderCount(user.getId(),nTime,nowTime);
        Long nSuccess = orderDao.getOrderCount(user.getId(),nTime,nowTime,1);
        Long nYOrders = orderDao.getOrderCount(user.getId(),nTime-86400000L,nowTime-86400000L);
        Long nYSuccess = orderDao.getOrderCount(user.getId(),nTime-86400000L,nowTime-86400000L,1);
        if (user.isAdmin()){
            nOrders = orderDao.getOrderCount(nTime,nowTime);
            nSuccess = orderDao.getOrderCount(nTime,nowTime,1);
            nYOrders = orderDao.getOrderCount(nTime-86400000L,nowTime-86400000L);
            nYSuccess = orderDao.getOrderCount(nTime-86400000L,nowTime-86400000L,1);
        }

        /**
         * 今日数据
         */
        JSONObject object = ResponseData.object("总流水",String.format("%.2f",tWater)+"元");
        object.put("订单数量", String.valueOf(tOrders));
        object.put("成功订单数量", String.valueOf(tSuccess));
        object.put("成功率", new Double(tSuccess.doubleValue() / tOrders.doubleValue() * 100).longValue()+"%");
        object.put("上一分钟数量", String.valueOf(lOrders));
        object.put("近一分钟数量", String.valueOf(nOrders));
        object.put("上一分钟成功订单", String.valueOf(lSuccess));
        object.put("近一分钟成订单", String.valueOf(nSuccess));
        object.put("上一分钟成功率", new Double(lSuccess.doubleValue() / lOrders.doubleValue() * 100).longValue()+"%");
        object.put("近一分钟成订率", new Double(nSuccess.doubleValue() / nOrders.doubleValue() * 100).longValue()+"%");
        JSONObject json = ResponseData.object("today", JSONObject.toJSONString(object));
        /**
         * 昨日数据
         */
        object = ResponseData.object("总流水",String.format("%.2f",yWater)+"元");
        object.put("订单数量", String.valueOf(yOrders));
        object.put("成功订单数量", String.valueOf(ySuccess));
        object.put("成功率", new Double(ySuccess.doubleValue() / yOrders.doubleValue() * 100).longValue()+"%");
        object.put("上一分钟数量", String.valueOf(lYOrders));
        object.put("近一分钟数量", String.valueOf(nYOrders));
        object.put("上一分钟成功订单", String.valueOf(lYSuccess));
        object.put("近一分钟成订单", String.valueOf(nYSuccess));
        object.put("上一分钟成功率", new Double(lYSuccess.doubleValue() / lYOrders.doubleValue() * 100).longValue()+"%");
        object.put("近一分钟成订率", new Double(nYSuccess.doubleValue() / nYOrders.doubleValue() * 100).longValue()+"%");
        json.put("yesterday", JSONObject.toJSONString(object));
        return ResponseData.success(json);
    }

    public ResponseData merchantDetails(User user, String ip, String serverName, int serverPort, String url) {
        System.out.printf("ip:%s serverName:%s serverPort:%d url:%s\n",ip,serverName,serverPort,url);
        if (user == null || StringUtils.isEmpty(user.getId())) return ResponseData.error();
        /**
         * 获取商户信息
         */
        JSONObject object = ResponseData.object("商户ID", user.getPid());
        object.put("商户密匙",user.getSecretKey());
        /**
         * 获取支付网关
         */
        StringBuilder sb = new StringBuilder();
        if (url.startsWith("https")){
            sb.append("https://").append(serverName);
        }else {
            sb.append("http://").append(serverName);
        }
        if(serverPort != 80){
            sb.append(":").append(serverPort);
        }
//        object.put("网关地址",sb+"/");
        object.put("对接文档",sb+"/swagger-ui.html");
        sb.append("/v3api/Polymer");
        object.put("兼容易支付网关地址",sb.toString());
        return ResponseData.success(JSONUtil.getSortJson(object));
    }
    private JSONObject getOrder(Order order){
        return getOrder(order,false);
    }
    private JSONObject getOrder(Order order, boolean admin){
        JSONObject object = ResponseData.object("ID", order.getId());
        if (admin){
            User user = userRepository.findAllById(order.getUid());
            object.put("商户","");
            if (user != null){
                object.put("商户",user.getUsername());
            }
        }
        object.put("商户订单号",order.getOutTradeNo());
        object.put("交易流水订单号",order.getTradeNo());
//        object.put("三方渠道","");
        object.put("支付方式","");
        ThirdParty party = thirdPartyRepository.findAllById(order.getThirdPartyId());
        if (party != null){
//            object.put("三方渠道",party.getTitle());
            PayType type = payTypeRepository.findAllById(party.getTypeId());
            if (type != null){
                object.put("支付方式",type.getName());
            }
        }
        object.put("订单金额",order.getMoney()+"元");
        object.put("实付金额",order.getTotalFee()+"元");
        object.put("手续费",order.getFee()+"元");

        object.put("订单通知状态",order.getNotifyState() == 1?"已通知":"未通知");
        object.put("订单交易状态",order.getState() == 1?"成功":"失败");

//        object.put("更新时间",TimeUtil.getDateTime(order.getUpdateTime()));
        object.put("下订单时间",TimeUtil.getDateTime(order.getAddTime()));
        return object;
    }
    private JSONArray getOrders(List<Order> orders){
        JSONArray array = new JSONArray();
        for (Order order: orders) {
            if(order != null){
                array.add(getOrder(order));
            }
        }
        return array;
    }
    public ResponseData orders(int page, User user, String ip) {
        if (user == null) return ResponseData.error();
        page--;
        if (page < 0) page=0;
        Pageable pageable = PageRequest.of(page,30, Sort.by(Sort.Direction.DESC,"addTime"));
        Page<Order> orderPage = orderRepository.findAllByUid(user.getId(),pageable);
        JSONObject object = ResponseData.object("total", orderPage.getTotalPages());
        object.put("list", getOrders(orderPage.getContent()));
        return ResponseData.success(object);
    }

    public ResponseData orderNotify(List<String> selected, User user, String ip) {
//        System.out.printf(selected.toString()+"\n");
        if (user == null) return ResponseData.error();
        if(!user.isAdmin()) return ResponseData.error("需要管理权限!");
        List<Order> orders = orderDao.getOrders(selected);
        if (orders == null) return ResponseData.error();
        for (Order order: orders) {
            User u = userRepository.findAllById(order.getUid());
            if (u != null){
                EPayNotify notify = adapterService.getEPayNotify(order,u);
                String url = order.getNotifyUrl();
                String result = ToolsUtil.request(url, EPayNotify.getParameter(notify));
//                System.out.printf("result:%s\n",result);
                if("success".equals(result)){
                    order.setNotifyState(1);
                    orderRepository.save(order);
                }
            }
        }
        return ResponseData.success(ResponseData.object("state",true));
    }

    public ResponseData orderDelete(List<String> selected, User user, String ip) {
        if (user == null) return ResponseData.error();
        if(!user.isAdmin()) return ResponseData.error("需要管理权限!");
        orderRepository.deleteAllById(selected);
        return ResponseData.success(ResponseData.object("state",true));
    }

    public ResponseData orderNo(int page, String orderNo, User user, String ip) {
        page--;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page,30,Sort.by(Sort.Direction.DESC,"addTime"));
        Page<Order> orderPage = orderDao.getOrders(orderNo,pageable);
        JSONObject object = ResponseData.object("total", orderPage.getTotalPages());
        object.put("list", getOrders(orderPage.getContent()));
        return ResponseData.success(object);
    }
}
