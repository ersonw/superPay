package com.example.superpay.data;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@ToString(includeFieldNames = true)
public class RequestHeader {
    private String ip;
    private String userAgent;
    private String token;
    private String serverName;
    private int serverPort;
    private String uri;
    private String url;
    private String schema;
    private String query;
    private String referer;
    private String openId;
    private String appId;
    private String unionId;
    private String fromOpenId;
    private String fromAppId;
    private String fromUnionId;
    private String wxEnv;
    private String wxSource;
    private String forwardedFor;
    private String user;
    private String client;

    public User getUser() {
        if (StringUtils.isEmpty(user)) return null;
        return JSONObject.toJavaObject(JSONObject.parseObject(user), User.class);
    }
}
