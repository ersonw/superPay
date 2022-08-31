package com.example.superpay.util;
import com.github.wxpay.sdk.WXPayConfig;
import lombok.Setter;

import java.io.*;
import java.util.Base64;

/**
 * 微信支付配置(单例)
 */
public class WXConfigUtil implements WXPayConfig {

    private byte[] certData;
    private static WXConfigUtil INSTANCE;


    private String APP_ID = "*****";//应用AppID
    private String KEY = "******";//商户密钥
    private String MCH_ID = "******";//商户号

    public WXConfigUtil(String app_id,String mch_id,String key, String cert) {
        this.APP_ID = app_id;
        this.MCH_ID = mch_id;
        this.KEY = key;
        this.certData = Base64.getDecoder().decode(cert);
    }
    public WXConfigUtil() {
//        this.APP_ID = "wx4fcd04bc73de65e1";
//        this.MCH_ID = "1501179181";
//        this.KEY = "192006250b4c09247ec02edce69f6a2d";
//        try{
//            String certPath = WXConfigUtil.class.getClassLoader().getResource("").getPath();
//            File file = new File(certPath+ "apiclient_cert.p12");
//            InputStream certStream = new FileInputStream(file);
//            this.certData = new byte[(int) file.length()];
//            certStream.read(this.certData);
//            certStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        this.certData = Base64.getDecoder().decode("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");
    }

    //双重检查加锁
    public static WXConfigUtil getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (WXConfigUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXConfigUtil();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public String getAppID() {
        return APP_ID;
    }

    //parnerid，商户号
    @Override
    public String getMchID() {
        return MCH_ID;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}