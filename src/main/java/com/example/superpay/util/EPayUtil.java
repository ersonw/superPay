package com.example.superpay.util;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.EPayData;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.ThirdParty;
import org.apache.commons.lang3.StringUtils;

public class EPayUtil {
    public static String submit(ThirdParty party, Order order,String type){
        StringBuilder sb = new StringBuilder();
        sb.append("<form name=\"punchout_form\" method=\"post\" action=\"").append(party.getDomain()).append("\">");
        EPayData ePay = new EPayData();
        ePay.setPid(new Long(party.getMchId()));
        ePay.setType(type);
        ePay.setOut_trade_no(order.getOutTradeNo());
        ePay.setMoney(String.valueOf(order.getMoney()));
        ePay.setNotify_url(party.getNotifyUrl());
        ePay.setReturn_url(party.getCallbackUrl());
        ePay.setSign(ePay.getSign(party.getSecretKey()));
        String sign = ePay.getSign();
        String sign_type = ePay.getSign_type();
        JSONObject object = EPayData.getObject(ePay);
        object.put("sign", sign);
        object.put("sign_type", sign_type);
        return sb.append(getBody(object)).append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n" +
                "</form>\n" +
                "<script>document.forms[0].submit();</script>").toString();
    }
    public static String getBody(JSONObject object){
        StringBuilder sb = new StringBuilder();
        for (String key: object.keySet()) {
            if (object.get(key) != null){
                sb.append("<input type=\"hidden\" name=\"").append(key).append("\" value=\"").append(object.get(key)).append("\">");
            }
        }
        return sb.toString();
    }
}
