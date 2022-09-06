package com.example.superpay.util;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;

import com.alipay.api.response.AlipayAssetPointBalanceQueryResponse;
import com.alipay.api.response.AlipayDataBillBalanceQueryResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.example.superpay.config.AlipayConfig;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.ThirdParty;
import org.apache.commons.lang3.StringUtils;

public class AlipayUtil {
    public static String alipayNative(ThirdParty thirdParty, Order order){
        AlipayConfig config = new AlipayConfig();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",thirdParty.getMchId(),thirdParty.getSecretKey(),"json",config.charset,thirdParty.getPublicKey(),config.sign_type);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(thirdParty.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOutTradeNo());
        bizContent.put("total_amount", order.getMoney());
        bizContent.put("subject", order.getOutTradeNo());
        bizContent.put("timeout_express","5m");
//        bizContent.put("time_expire","15m");
        request.setBizContent(bizContent.toString());
        try{
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
//                System.out.println("调用成功");
                return response.getQrCode();
            } else {
                System.out.println("调用失败");
            }
//            System.out.println(response.getQrCode());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String alipay(ThirdParty thirdParty, Order order){
        AlipayConfig config = new AlipayConfig();
        config.alipay_public_key = thirdParty.getPublicKey();
        config.merchant_private_key = thirdParty.getSecretKey();
        config.app_id = thirdParty.getMchId();
//        if (StringUtils.isNotEmpty(thirdParty.getDomain())){
//            AlipayConfig.gatewayUrl = thirdParty.getDomain();
//        }
        config.return_url = thirdParty.getCallbackUrl();
        config.notify_url = thirdParty.getNotifyUrl();
        return alipay(order.getOutTradeNo(),order.getMoney().toString(),order.getOutTradeNo(),order.getOutTradeNo(),config);
    }
    /**
     * @Title: alipay
     * @Description: TODO(支付)
     * @param outTradeNo  商户订单号，商户网站订单系统中唯一订单号，必填   对应缴费记录的orderNo
     * @param totalAmount  付款金额，必填
     * @param subject 主题
     * @param body 商品描述，可空
     * @param: @return
     * @param: @throws Exception
     * @return: String
     * @throws
     */
    public static String alipay(String outTradeNo,String totalAmount,String subject,String body, AlipayConfig config){
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(config.gatewayUrl, config.app_id,
                config.merchant_private_key, "json", config.charset, config.alipay_public_key,
                config.sign_type);


        //该笔订单允许的最晚付款时间
        String timeout="5m";
        //设置请求参数
        String content = "{\"out_trade_no\":\""+ outTradeNo +"\","
                + "\"total_amount\":\""+ totalAmount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"timeout_express\":\""+ timeout +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\""+config.productCode+"\"}";
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(config.return_url);
        alipayRequest.setNotifyUrl(config.notify_url);
        alipayRequest.setBizContent(content);

        //请求
        String result="";
        try {
            result = alipayClient.pageExecute(alipayRequest).getBody();
            return result;
        } catch (AlipayApiException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return null;
    }
    public static boolean userTransfer(ThirdParty thirdParty){
        CertAlipayRequest alipayConfig = new CertAlipayRequest();
        alipayConfig.setPrivateKey(thirdParty.getSecretKey());
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
        alipayConfig.setAppId(thirdParty.getMchId());
        alipayConfig.setCharset("UTF8");
        alipayConfig.setSignType("RSA2");
        alipayConfig.setEncryptor("");
        alipayConfig.setFormat("json");
        alipayConfig.setCertContent(thirdParty.getAppPublicKey());
        alipayConfig.setAlipayPublicCertContent(thirdParty.getPublicKey());
        alipayConfig.setRootCertContent(thirdParty.getRootKey());
        try{
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
            AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
            AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
            model.setOutBizNo(TimeUtil._getOrderNo());
            model.setRemark("代发"+model.getOutBizNo());
            model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");
            model.setBizScene("DIRECT_TRANSFER");
            Participant payeeInfo = new Participant();
            payeeInfo.setIdentity("godword@foxmail.com");
            payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
//        payeeInfo.setIdentity("2088123412341234");
//        payeeInfo.setIdentityType("ALIPAY_USER_ID");
            payeeInfo.setName("吴茂名");
            model.setPayeeInfo(payeeInfo);
            model.setTransAmount("1.00");
            model.setProductCode("TRANS_ACCOUNT_NO_PWD");
            model.setOrderTitle("代发"+model.getOutBizNo());
            request.setBizModel(model);
            AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
            System.out.println(response.getBody());
            if (response.isSuccess()) {
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String aliBalance(ThirdParty thirdParty){
        AlipayConfig config = new AlipayConfig();
        config.alipay_public_key = thirdParty.getPublicKey();
        config.merchant_private_key = thirdParty.getSecretKey();
        config.app_id = thirdParty.getMchId();
        config.return_url = thirdParty.getCallbackUrl();
        config.notify_url = thirdParty.getNotifyUrl();
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", config.app_id, config.merchant_private_key, "json", config.charset, config.alipay_public_key, config.sign_type);
//        AlipayAssetPointBalanceQueryRequest queryRequest = new AlipayAssetPointBalanceQueryRequest();
        AlipayDataBillBalanceQueryRequest queryRequest = new AlipayDataBillBalanceQueryRequest();
//        queryRequest.setBizContent("{" +
//                "  \"bill_user_id\":\""+config.app_id+"\"" +
//                "}");
        try{
            AlipayDataBillBalanceQueryResponse response = alipayClient.execute(queryRequest);
            if(response.isSuccess()){
                System.out.println("调用成功");
                return response.getAvailableAmount();
            } else {
                System.out.println("调用失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
//    /**
//     * 支付宝退款接口
//     * @param outTradeNo
//     * @param tradeNo
//     * @param refundAmount
//     * @param refundReason
//     * @param out_request_no  标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
//     * @return
//     */
//    public static String aliRefund(String outTradeNo,String tradeNo,String refundAmount,String refundReason,String out_request_no) {
//        //获得初始化的AlipayClient
//        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
//
//        //设置请求参数
//        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
//        alipayRequest.setReturnUrl(AlipayConfig.return_url);
//        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
//        try {
//            alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
//                    + "\"trade_no\":\""+ tradeNo +"\","
//                    + "\"refund_amount\":\""+ refundAmount +"\","
//                    + "\"refund_reason\":\""+ refundReason +"\","
//                    + "\"out_request_no\":\""+ out_request_no +"\"}");
//
//            //请求
//            String result;
//
//            //请求
//            result = alipayClient.execute(alipayRequest).getBody();
//            System.out.println("*********************\n返回结果为："+result);
//            return result;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return null;
//        }
//    }
    public static boolean checkSign(HttpServletRequest req, ThirdParty thirdParty) {
        AlipayConfig config = new AlipayConfig();
        config.alipay_public_key = thirdParty.getPublicKey();
        config.merchant_private_key = thirdParty.getSecretKey();
        config.app_id = thirdParty.getMchId();
        return checkSign(req,config);
    }
    /**
     * 支付宝的验签方法
     * @param req
     * @return
     */

    public static boolean checkSign(HttpServletRequest req, AlipayConfig config) {
        Map<String, String[]> requestMap = req.getParameterMap();
        Map<String, String> paramsMap = new HashMap<>();
        requestMap.forEach((key, values) -> {
            if(!key.equals("ip")
                    && !key.equals("serverName")
                    && !key.equals("serverPort")
                    && !key.equals("uri")
                    && !key.equals("url")
                    && !key.equals("schema")
                    && !key.equals("user")){
                StringBuilder strs = new StringBuilder();
                for(String value : values) {
                    strs.append(value);
                }
//                System.out.println(("key:"+key+" value为："+strs));
                if (StringUtils.isNotEmpty(strs.toString())){
                    paramsMap.put(key, strs.toString());
                }
            }
        });

        //调用SDK验证签名
        try {
            return  AlipaySignature.rsaCheckV1(paramsMap, config.alipay_public_key, config.charset, config.sign_type);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("*********************验签失败********************");
            return false;
        }
    }
}
