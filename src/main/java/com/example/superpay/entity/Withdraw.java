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
    private String uid=null;
    private String typeId=null;
    private Double money=0D;
    private Double totalFee=0D;
    private Double fee=0D;
    private String name=null;
    private String account=null;
    private String remark=null;
    private int state = 0;
    private long addTime=0;
    private long updateTime=0;
}
