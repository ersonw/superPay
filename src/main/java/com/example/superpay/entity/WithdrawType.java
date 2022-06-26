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
    private String title;
    private Double rate;
    private Double mini;
    private Double max;
    private int state;
    private long addTime;
    private long updateTime;
}
