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
    private Double money=0D;
    private Double totalFee=0D;
    private Double fee=0D;
    private int notifyState=0;
    private long state =0;
    private long addTime=0L;
    private long updateTime=0L;
    public Order() {
        this.id = ToolsUtil.getToken();
    }
}
