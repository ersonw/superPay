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
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String uid;
    private String orderId;
    private String tradeNo;
    private String outTradeNo;
    private String thirdPartyId;
    private String ip;
    private String returnUrl;
    private String notifyUrl;
    private String name;
    private Double money;
    private Double totalFee;
    private Double fee;
    private int notifyState;
    private long state =0;
    private long addTime;
    private long updateTime;
    public Order() {
        this.id = ToolsUtil.getToken();
    }
}
