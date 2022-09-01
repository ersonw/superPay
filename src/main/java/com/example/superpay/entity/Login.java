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
@Document(collection = "login")
public class Login {
    @Id
    private String id;
    @Field("uid")
    private String uid=null;
    @Field("ip")
    private String ip=null;
    @Field("addTime")
    private long addTime=0L;
    public Login(){
        this.id = ToolsUtil.getToken();
    }
    public Login(String uid, String ip){
        this.uid = uid;
        this.ip = ip;
        this.addTime=System.currentTimeMillis();
        this.id = ToolsUtil.getToken();
    }
}
