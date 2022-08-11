package com.example.superpay.config;

/**
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2019-04-30
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2018031602385705";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvTpNi61GjIhFrdRaPPyIlhHeZ3TPUkGvjoPKb2Yptwe8uy5YS8p6jnTohSJp0aIvA8SduBCZNfgzNq32bOXwWrdar3tPNQj0t62A7ucwFhxoMFMtnbzOjCIEKZWWC3L3J6WaNstchl35DL0RupVWOYJ5j27MfHch4IoLzanZxpRRzuLE3YMsvSL5tNES6rNqlPSfiAiPvLV7mWLByWDJJVW5+A5oFlfeFD2Ey0tzlEFslPvvVKhP0thByyEqn2d8//6rkieKjEM/BsFAXjl40RUWeUCRorS7o1TSZC9Hn9azjvOFF2sgOrsd5HG4Lhh9WxvblZ1AshwZxeNe/ZustAgMBAAECggEADlksDwibofKD4nuu4QKV1ORGtb05JMi9S+A8ey0O+3TIEthu7BYXjeSsgVTj72svJReX1pVYXTdX7O2AVlgaI/EOhPqz8zTctQly0vCeFkW8iAibrVeYrltf1G4AJPnUPtZvomFk4kb3+p+/xh6aJhEaZanxuzZA1jRc63dnQl3RjnvjSqcE7deawK096rMlBr82XoEWltoN6xpUuLqoJ/nwqS5EXd54fEDc4sTZgGHW1A5WOp/t1+UVfoG4C+0pl1uiC5eALl1rlzdx23IOEU5V4FAwTDNfb442B/i+U/FglQwQw62yPkz4J5e9xGuEVbvtPZmvtInbYRnXaIxFAQKBgQDzkb8AQ02ZK5+QE5no0M0YOrBB8DnFekTeuy7OjuhKml2NjVVNXDkEd+z6/2n/RfSBiFsgEOPMizRgAF0esByuESzjnvsXZ2BFZRlQ7Ni7+U6aPXnY8gdnkIvS/5V4F83yBqLBz3eC+GPajnUXSZwc1z1bnhx1m15rRNFX6cqBfQKBgQC4QPnMIZcg+54UkJVFlK5ULOCmrILUDiwLFxth1nxFe1MZfTxypQcfNexRyrZb8KfRsgZNf6M53dE6rgSJDK46tAPF+O6jPtvILwKdSXLvxGTb7pQ2ovPgBbz+CQH7SKDthypDXUMbR5VCmtyJJeK4LW/iOKTluV1MkomxvJ4/cQKBgER6EEHJqjJK4mRGLnoW4eJS9aTEHenYEy6vX1xxLvtyZKTcPEQwjlMkSDrUvf8nsrMMG9prBTBHXqUy1PtAtf92ErG3y43r4VQBNVncDJ7kW2XfrLcCbHSAXd8nPeVyg9LsbKuiYU4v+RrD/EVcy4gMN1Lfo86orKXpxhU6RFWdAoGAXDiZTqSZYfbOfniHXhY20wbLQmEh8kVNohdkqymRda1uQFnAgZk74VE6AQ43C/l95aT3Jp718aambHpg5r+kDNnA8bvQpYB2vNFau6LhlkR0PuhA4r/Y1I3KtFOJ3F3Tvk9ixejOB79iY73jF/oQaiLD1zSGxDxtCEBoDr/bbOECgYBQHaza5KpWbu1dNSfXe0hb15vWaRlawEWogqoBOwmcGe06wXnuy6IC26qKVo6oCWFABzHjMtNADxwEpTqxwoFqrRBHtPvc9fc7L3F1ARfa1Wq6lZBzdMEZ4ZfCyhFcFg5C+HrLtwjOH1o3GCdTtRG+WK8IstFPxK00pG3bBG7/ZA==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0h5BDdwC3b4rLJDuUwf7pRat3UT9EaJJUh41jYTiqKKsHHBWUiJo/znjqYRn9A5Lx76XtLXXyfEM4DxffqEzu1GTDb3BMc/mZDNXe94xqqAzvC2waMEgP7gzBW/SSNjV0cTLpRLuL34kvgG4mHcGQX9ea9Ux6Oo5jdzr7Da4bccNH2abznrzgB2w+qY21UHHt27JgA0XaRecTrSYABHf95dOWPIKyId03IDWJe58N51/jFm8Ku5r0KFZg4VdU/Cv2HVm2QEnmUyQJbLVo+yVJCBeTbT9epKakq+20LUNMmv+PwWyW8qw7XgP+8NlX7VLHlsbsJY2jKcfXpmeEBsRFQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "https://www.alipay.com";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，沙箱环境下可以填写本地地址
    public static String return_url = "http://localhost:8082/web/pay/callback";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";

    //销售产品码，商家和支付宝签约的产品码,默认值为：QUICK_WAP_WAY
    public static String productCode ="QUICK_WAP_WAY";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


}
