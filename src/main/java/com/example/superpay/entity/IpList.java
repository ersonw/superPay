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
@Document(collection = "ipList")
public class IpList {
    public IpList(){
        this.id = ToolsUtil.getToken();
    }
    public IpList(String uid,String address){
        this.uid = uid;
        this.address = address;
        this.id = ToolsUtil.getToken();
        this.addTime = System.currentTimeMillis();
    }
    @Id
    private String id;
    @Field("uid")
    private String uid;
    private String address;
    private long addTime;
    private long updateTime=0L;
}
