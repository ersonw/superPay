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
    private String uid;
    @Field("ip")
    private String ip;
    @Field("add_time")
    private long add_time;
    public Login(){
        this.id = ToolsUtil.getToken();
    }
    public Login(String uid, String ip){
        this.uid = uid;
        this.ip = ip;
        this.add_time=System.currentTimeMillis();
        this.id = ToolsUtil.getToken();
    }
}
