package com.example.superpay.entity;

import com.example.superpay.util.ToolsUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString(includeFieldNames = true)
@Setter
@Getter
@Document(collection = "payType")
public class PayType {
    public PayType(){
        id = ToolsUtil.getToken();
    }
    @Id
    private String id;
    private String name=null;
    private String type=null;
    private long addTime=0L;
    private long updateTime=0L;
}
