package com.example.superpay.entity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.annotation.Id;
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
    @Field("username")
    private String username;
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
    private Integer rate;
    @Field("fee")
    private Integer fee;
    @Field("state")
    private Integer state;
    @Field("addTime")
    private Long  addTime;
    @Field("updateTime")
    private Long updateTime;

    public User() {
        UUID uuid = UUID.randomUUID();
        id = uuid.toString().replaceAll("-","");
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static Object getInstance(String user) {
        if (user != null){
            return JSONObject.toJavaObject(JSONObject.parseObject(user),User.class);
        }
        return null;
    }
}
