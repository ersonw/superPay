package com.example.superpay.data;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.util.JSONUtil;
import com.example.superpay.util.MD5Util;
import com.example.superpay.util.ToolsUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.*;

@Setter
@Getter
@ToString(includeFieldNames = true)
@Data
@ApiModel
public class EPayData {
    public EPayData(){}
    @ApiModelProperty(value= "商户ID",required = false)
    private long pid;
    @ApiModelProperty(value= "支付方式",required = false)
    private String type;
    @ApiModelProperty(value= "商户订单号",required = false)
    private String out_trade_no;
    @ApiModelProperty(value= "异步通知地址",required = false)
    private String notify_url;
    @ApiModelProperty(value= "跳转通知地址",required = false)
    private String return_url;
    @ApiModelProperty(value= "商品名称",required = false)
    private String name;
    @ApiModelProperty(value= "商品金额",required = false)
    private String money;
    @ApiModelProperty(value= "网站名称",required = false)
    private String sitename;
    @ApiModelProperty(value= "签名字符串",required = false)
    private String sign;
    @ApiModelProperty(value= "签名类型",required = false)
    private String sign_type = "MD5";

    @ApiModelProperty(hidden = true)
    private String ip;
    @ApiModelProperty(hidden = true)
    private String referer;

    @Override
    public String toString() {
        JSONObject object = getObject();
        object = JSONUtil.getSortJson(object);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry: object.entrySet()) {
            if (entry.getValue() != null){
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        if (sb.length() > 0){
            return sb.substring(0, sb.length()-1);
        }
        return "";
    }
    private JSONObject getObject(){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(this));
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry: object.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue().toString()) &&
                    !entry.getKey().equals("sign") &&
                    !entry.getKey().equals("sign_type") &&
//                    !entry.getKey().equals("name") &&
//                    !entry.getKey().equals("sitename") &&
                    !entry.getKey().equals("ip")){
                json.put(entry.getKey(), entry.getValue());
            }
        }
        return json;
    }
    public static JSONObject getObject(EPayData data){
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(data));
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Object> entry: object.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue().toString()) &&
                    !entry.getKey().equals("sign") &&
                    !entry.getKey().equals("sign_type") &&
//                    !entry.getKey().equals("name") &&
//                    !entry.getKey().equals("sitename") &&
                    !entry.getKey().equals("ip")){
                json.put(entry.getKey(), entry.getValue());
            }
        }
        return json;
    }
    public String getSign(String key) {
        return ToolsUtil.md5(StringEscapeUtils.unescapeJava(this.toString()) + key);
//        if (this.getSign_type().equalsIgnoreCase("MD5")){
//            return DigestUtils.md5DigestAsHex((this.toString() + key).getBytes());
//        }
//        if(this.getSign_type().equalsIgnoreCase("WXSING")){
//            return ToolsUtil.getWxSign(JSONUtil.toStringMap(this.getObject()),key);
//        }
//        return null;
    }
    public boolean isSign(String key) {
        if(this.getSign() == null) return false;
//        System.out.printf("%s\n", StringEscapeUtils.unescapeJava(this.toString())+key);
//        System.out.printf("%s\n", this.getSign(key));
        return this.getSign(key).equals(this.getSign());
    }
}
