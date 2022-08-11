package com.example.superpay.util;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import com.example.superpay.config.AlipayConfig;
import com.example.superpay.entity.Order;
import com.example.superpay.entity.ThirdParty;
import org.apache.commons.lang3.StringUtils;

public class AlipayUtil {

    public static String alipay(ThirdParty thirdParty, Order order){
        AlipayConfig.alipay_public_key = thirdParty.getPublicKey();
        AlipayConfig.merchant_private_key = thirdParty.getSecretKey();
        AlipayConfig.app_id = thirdParty.getMchId();
        if (StringUtils.isNotEmpty(thirdParty.getDomain())){
            AlipayConfig.gatewayUrl = thirdParty.getDomain();
        }
        AlipayConfig.return_url = thirdParty.getCallbackUrl();
        AlipayConfig.notify_url = thirdParty.getNotifyUrl();
        return AlipayUtil.alipay(order.getOutTradeNo(),order.getMoney().toString(),order.getOutTradeNo(),"生活服务");
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
    public static String alipay(String outTradeNo,String totalAmount,String subject,String body){
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);


        //该笔订单允许的最晚付款时间
        String timeout="30m";
        //设置请求参数
        String content = "{\"out_trade_no\":\""+ outTradeNo +"\","
                + "\"total_amount\":\""+ totalAmount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"timeout_express\":\""+ timeout +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\""+AlipayConfig.productCode+"\"}";
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
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

    /**
     * 支付宝退款接口
     * @param outTradeNo
     * @param tradeNo
     * @param refundAmount
     * @param refundReason
     * @param out_request_no  标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     * @return
     */
    public static String aliRefund(String outTradeNo,String tradeNo,String refundAmount,String refundReason,String out_request_no) {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        try {
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
                    + "\"trade_no\":\""+ tradeNo +"\","
                    + "\"refund_amount\":\""+ refundAmount +"\","
                    + "\"refund_reason\":\""+ refundReason +"\","
                    + "\"out_request_no\":\""+ out_request_no +"\"}");

            //请求
            String result;

            //请求
            result = alipayClient.execute(alipayRequest).getBody();
            System.out.println("*********************\n返回结果为："+result);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public static boolean checkSign(HttpServletRequest req, ThirdParty thirdParty) {
        AlipayConfig.alipay_public_key = thirdParty.getPublicKey();
        AlipayConfig.merchant_private_key = thirdParty.getSecretKey();
        AlipayConfig.app_id = thirdParty.getMchId();
        return checkSign(req);
    }
    /**
     * 支付宝的验签方法
     * @param req
     * @return
     */

    public static boolean checkSign(HttpServletRequest req) {
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
                paramsMap.put(key, strs.toString());
            }
        });

        //调用SDK验证签名
        try {
            return  AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("*********************验签失败********************");
            return false;
        }
    }
}
