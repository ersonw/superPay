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
    private String title=null;
    private String domain=null;
    private String mchId=null;
    private String callbackUrl=null;
    private String notifyUrl=null;
    private String errorUrl=null;
    private String secretKey=null;
    private String publicKey=null;
    private String appid=null;
    private String typeId=null;
    private int third = 0;
    private int state=0;
    private Double rate=0D;
    private long addTime=0L;
    private long updateTime=0L;
}
