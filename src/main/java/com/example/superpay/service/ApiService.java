package com.example.superpay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.superpay.dao.OrderDao;
import com.example.superpay.dao.WithDrawDao;
import com.example.superpay.data.ResponseData;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.User;
import com.example.superpay.repository.*;
import com.example.superpay.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThirdPartyRepository thirdPartyRepository;
    @Autowired
    private PayTypeRepository payTypeRepository;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private WithDrawDao withDrawDao;
    @Autowired
    private LoginRepository loginRepository;

    public ResponseData dashboard(User user, String ip) {
//        Pageable pageable = PageRequest.of(0, 1);
//        Page<Order> orderPage = orderRepository.findAllByAddTimeGreaterThanEqual(1656241208239L,pageable);
//        System.out.printf(orderPage.getTotalElements()+"\n");
        if (user == null) return ResponseData.error("用户不存在！");
        JSONArray array = new JSONArray();
        /**
         * 获取今日 昨日收款
         */
        Double tMoney = orderDao.getOrderSum(user.getId(),TimeUtil.getTodayZero(),TimeUtil.getAfterDaysZero(1));
        Double yMoney = orderDao.getOrderSum(user.getId(),TimeUtil.getBeforeDaysZero(1),TimeUtil.getTodayZero());
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
        object = ResponseData.object("title","可提现");
        object.put("context", String.format("%.2f",aTotalFee-withDraw)+"元");
        object.put("bottomLeft","已提现");
        object.put("bottomRight",String.format("%.2f",aWithDraw)+"元");
        object.put("percentage",new Double(withDraw / aTotalFee * 100).longValue());
        array.add(object);
        return ResponseData.success(array);
    }

    public ResponseData dayData(User user, String ip) {
        long tTime = TimeUtil.getTodayZero();
        long aTime = TimeUtil.getAfterDaysZero(1);
        long yTime = TimeUtil.getBeforeDaysZero(1);
        /**
         * 获取总流水
         */
        Double tWater = orderDao.getOrderSum(user.getId(),tTime,aTime);
        Double yWater = orderDao.getOrderSum(user.getId(),yTime,tTime);
        /**
         * 获取总订单
         */
        Long tOrders = orderDao.getOrderCount(user.getId(),tTime,aTime);
        Long yOrders = orderDao.getOrderCount(user.getId(),yTime,tTime);
        /**
         * 获取总成功量
         */
        Long tSuccess = orderDao.getOrderCount(user.getId(),tTime,aTime,1);
        Long ySuccess = orderDao.getOrderCount(user.getId(),yTime,tTime,1);

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

        /**
         * 获取近一分钟
         */
        Long nOrders = orderDao.getOrderCount(user.getId(),nTime,nowTime);
        Long nSuccess = orderDao.getOrderCount(user.getId(),nTime,nowTime,1);
        Long nYOrders = orderDao.getOrderCount(user.getId(),nTime-86400000L,nowTime-86400000L);
        Long nYSuccess = orderDao.getOrderCount(user.getId(),nTime-86400000L,nowTime-86400000L,1);

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
}
