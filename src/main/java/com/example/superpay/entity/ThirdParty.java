package com.example.superpay.entity;

import com.example.superpay.util.ToolsUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@ToString(includeFieldNames = true)
@Setter
@Getter
@Document(collection = "ThirdParty")
public class ThirdParty {
    public ThirdParty(){
        id = ToolsUtil.getToken();
    }
    @Id
    private String id;
    @Field("title")
    private String title;
    private String domain;
    private String mchId;
    private String callbackUrl;
    private String notifyUrl;
    private String errorUrl;
    private String secretKey;
    private String publicKey;
    private String appid;
    private String typeId;
    private int third = 0;
    private int state=0;
    private Double rate;
    private long addTime;
    private long updateTime;
}
