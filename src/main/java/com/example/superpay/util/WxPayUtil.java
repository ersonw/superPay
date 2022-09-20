package com.example.superpay.util;

import com.example.superpay.entity.Order;
import com.example.superpay.entity.ThirdParty;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WxPayUtil {
    public static String wxNative(ThirdParty thirdParty, Order order,String ip){
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(thirdParty.getAppid());
        payConfig.setMchId(thirdParty.getMchId());
        payConfig.setMchKey(thirdParty.getSecretKey());
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
//        payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
//        payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
//        payConfig.setKeyPath("classpath://apiclient_cert.p12");
        payConfig.setKeyContent(Base64.getDecoder().decode(thirdParty.getPublicKey()));
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setBody("聚合码收款");
        wxPayUnifiedOrderRequest.setProductId(order.getOutTradeNo());
        wxPayUnifiedOrderRequest.setOutTradeNo(order.getOutTradeNo());
        wxPayUnifiedOrderRequest.setTotalFee(new Double(order.getMoney() * 100).intValue());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(ip);
        wxPayUnifiedOrderRequest.setNotifyUrl(thirdParty.getNotifyUrl());
        wxPayUnifiedOrderRequest.setTradeType("NATIVE");
        wxPayUnifiedOrderRequest.setTimeExpire(TimeUtil.getWxTimeExpire(System.currentTimeMillis() + 1000 * 60 * 5));
//        wxPayUnifiedOrderRequest.setOpenid("oU5Ta5f9Vx6f-***********");
        try{
//            System.out.println(wxPayService.unifiedOrder(wxPayUnifiedOrderRequest));
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
            if(StringUtils.isNotEmpty(result.getCodeURL())){
                return result.getCodeURL();
            }
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String wxPayH5s(ThirdParty thirdParty, Order order,String ip) {
        try {
            Map< String, String > maps = new HashMap<>( );
            maps.put("out_trade_no", order.getOutTradeNo());
            maps.put("spbill_create_ip", ip);
            maps.put("total_fee", String.valueOf(new Double(order.getMoney() * 100).longValue()));
            maps.put("trade_type", "MWEB");
            maps.put("notify_url", thirdParty.getNotifyUrl());
            maps.put("body", "聚合支付收款："+order.getOutTradeNo());
            maps.put("time_expire",TimeUtil.getWxTimeExpire(System.currentTimeMillis() + 1000 * 60 * 5));
            WXPay wxPay = new WXPay( new WXConfigUtil(thirdParty.getAppid(),thirdParty.getMchId(),thirdParty.getSecretKey(),thirdParty.getPublicKey()));
            Map< String, String > reqData = wxPay.fillRequestData(maps);

            Map< String, String > map = wxPay.unifiedOrder(reqData);
            if(map.get("return_code").equals("FAIL")) return null;
//            System.out.println(map);
            String mWebUrl = map.get("mweb_url");
            //把成功后的跳转页面进行urlencoding转码 https://www.sojson.com/encodeurl.html
            String redirectUrl = thirdParty.getCallbackUrl()+order.getOutTradeNo();

            mWebUrl += "&redirect_url=" + UrlUtil.encode(redirectUrl);

            return mWebUrl;
        } catch (Exception e) {
            e.printStackTrace( );
        }
        return null;
    }
    /**
     * 微信支付 回调函数
     */
    public static boolean notifyUrl(ThirdParty thirdParty, Map< String, String > notifyMap) {//读取参数
        try {
            WXPay wxpay = new WXPay( new WXConfigUtil(thirdParty.getAppid(),thirdParty.getMchId(),thirdParty.getSecretKey(),thirdParty.getPublicKey()));
            //验证签名是否正确
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                //支付成功
                if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                    return true;
                    //商户订单号
//                String out_trade_no = notifyMap.get("out_trade_no");
//                Student order = Student.dao.findFirst("select * from student where wx_order_number = ? ", out_trade_no);
//                order.setIspay(1);
//                order.setPayMethod(2);
//                order.setPayTime(new Date( ));
//                order.update( );
                    //把订单状态改成已付款
                } else {
                    //log.info("微信支付回调函数：支付失败");
                }
            } else {
                //log.info("微信支付回调函数：微信签名错误");
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
