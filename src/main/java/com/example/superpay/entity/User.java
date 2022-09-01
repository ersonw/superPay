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
    private long pid=0;
    @Field("username")
    private String username=null;
    @Field("avatar")
    private String avatar=null;
    @Field("password")
    private String password=null;
    @Field("salt")
    private String salt=null;
    @Field("secretKey")
    private String secretKey=null;
    @Field("callbackUrl")
    private String callbackUrl=null;
    @Field("notifyUrl")
    private String notifyUrl=null;
    @Field("rate")
    private Double rate = 0D;
    @Field("fee")
    private Double fee = 0D;
    @Field("state")
    private Integer state = 0;
    @Field("addTime")
    private Long  addTime=0L;
    @Field("updateTime")
    private Long updateTime = 0L;
    private boolean admin =false;
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
