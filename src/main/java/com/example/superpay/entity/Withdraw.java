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
@Document(collection = "withdraw")
public class Withdraw {
    public Withdraw(){
        this.id = ToolsUtil.getToken();
    }
    @Id
    private String id;
    @Field("uid")
    private String uid;
    private String typeId;
    private Double money;
    private Double totalFee;
    private Double fee;
    private String name;
    private String account;
    private String remark;
    private int state = 0;
    private long addTime;
    private long updateTime;
}
