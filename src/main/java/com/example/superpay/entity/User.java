package com.example.superpay.entity;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.util.ToolsUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.*;

import java.util.UUID;

@ToString(includeFieldNames = true)
@Setter
@Getter
@Document(collection = "user")
public class User {
    @Id
    private String id;
    @Field("pid")
    private long pid;
    @Field("username")
    private String username;
    @Field("avatar")
    private String avatar;
    @Field("password")
    private String password;
    @Field("salt")
    private String salt;
    @Field("secretKey")
    private String secretKey;
    @Field("callbackUrl")
    private String callbackUrl;
    @Field("notifyUrl")
    private String notifyUrl;
    @Field("rate")
    private Double rate = 0D;
    @Field("fee")
    private Double fee = 0D;
    @Field("state")
    private Integer state = 0;
    @Field("addTime")
    private Long  addTime;
    @Field("updateTime")
    private Long updateTime;
    @Transient
    private String token;

    public User() {
        id = ToolsUtil.getToken();
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static User getUser(String user) {
        if (user != null){
            return JSONObject.toJavaObject(JSONObject.parseObject(user),User.class);
        }
        return null;
    }
}
