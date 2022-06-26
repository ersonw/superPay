package com.example.superpay.util;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.ToPayNotify;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.ThirdParty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ThirdPartyUtil {
    public static String toPay(Order order, ThirdParty showPay) {
        try {
            //支付post下单地址
            String url = showPay.getDomain();
            String mchid = showPay.getMchId();//商户号
            String secretKey = showPay.getSecretKey();//商户秘钥
            String money = String.valueOf(order.getMoney());
//            //金额换算成分单位
//            BigDecimal rate = new BigDecimal(100);
//            Integer total_fee = new BigDecimal(money).multiply(rate).intValue();
//            System.out.println(total_fee);
//            String out_trade_no = UUID.randomUUID().toString().replace("-", "");

            Map<String, String> data = new HashMap<>();
            data.put("mchid", mchid);//商户号
//            data.put("total_fee", String.valueOf(total_fee));//金额单位分
            data.put("total_fee", String.valueOf(order.getMoney() * 100));//金额单位分
            data.put("out_trade_no", order.getOutTradeNo());//用户自定义订单号
            data.put("callback_url", showPay.getCallbackUrl()+"?outTradeNo="+order.getOutTradeNo());//支付成功同步跳转地址
            data.put("notify_url", showPay.getNotifyUrl());//支付成功异步通知地址
            data.put("error_url", showPay.getErrorUrl());//支付失败或者取消同步跳转地址

            String _sign = ToolsUtil.getWxSign(data, secretKey);
            data.put("sign", _sign);//签名
//            System.out.println(data);
            String result = ToolsUtil.doPost(url, data);
            if (org.springframework.util.StringUtils.isEmpty(result) || result.indexOf("code") < 0) {
                System.out.println("下单请求错误");
            } else {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.getInteger("code") == 0) {
                    JSONObject data2 = jsonObject.getJSONObject("data");

                    //支付地址拼接
                    String payUrl = data2.getString("payUrl");
                    order.setOrderId(data2.getString("orderId"));
//                    System.out.println(payUrl);
                    //使用302跳转到支付地址
                    return payUrl;
                } else {
                    System.out.println(result);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static boolean toPayVerify(ToPayNotify toPayNotify, ThirdParty showPay) {

        String secretKey = showPay.getSecretKey();//商户秘钥

        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", toPayNotify.getOut_trade_no());
        map.put("trade_no", toPayNotify.getTrade_no());
        map.put("total_fee", toPayNotify.getTotal_fee());
        map.put("mchid", toPayNotify.getMchid());

        String str_sign = ToolsUtil.getWxSign(map, secretKey);
        String sign = toPayNotify.getSign();
        if (sign.equals(str_sign)) {
            return true;
        }
        System.out.println("签名错误");
        return false;
    }

}
