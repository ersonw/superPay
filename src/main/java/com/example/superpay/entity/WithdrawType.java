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
public class WithdrawType {
    public WithdrawType(){
        this.id = ToolsUtil.getToken();
    }
    @Id
    private String id;
    @Field("title")
    private String title=null;
    private Double rate=0D;
    private Double mini=0D;
    private Double max=0D;
    private int state=0;
    private long addTime=0;
    private long updateTime=0;
}
