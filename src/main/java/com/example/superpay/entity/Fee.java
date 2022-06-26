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
@Document(collection = "fee")
public class Fee {
    public Fee(){
        this.id = ToolsUtil.getToken();
    }
    @Id
    private String id;
    @Field("uid")
    private String uid;
    private String orderId;
    private String tip;
    private Double partyFee=0D;
    private Double rateFee=0D;
    private Double selfFee=0D;
    private long addTime;

    public boolean isNotEmpty(){
        if(this.partyFee >0 || this.rateFee>0 || this.selfFee > 0) return true;
        return false;
    }
}
