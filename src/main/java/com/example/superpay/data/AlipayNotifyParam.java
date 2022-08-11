package com.example.superpay.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@Setter
@Getter
@ToString(includeFieldNames = true)
public class AlipayNotifyParam {

    private String charset;//编码格式
    private String out_trade_no;//商户网站唯一订单号
    private String trade_no;//该交易在支付宝系统中的交易流水号。最长64位
    private String method;
    private BigDecimal total_amount;//该笔订单的资金总额
    private String sign;//签名结果
    private String auth_app_id;
    private String app_id;//支付宝分配给开发者的应用Id
    private String sign_type;//签名类型
    private String seller_id;//收款支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
    private String timestamp;//时间

    //get和set省略
}
