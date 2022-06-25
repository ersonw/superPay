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
    @Field("uid")
    private String uid;
    @Field("orderId")
    private String orderId;
    @Field("trade_no")
    private String tradeNo;
    @Field("out_trade_no")
    private String outTradeNo;
    @Field("third_party_id")
    private String thirdPartyId;
    @Field("ip")
    private String ip;
    @Field("return_url")
    private String returnUrl;
    @Field("notify_url")
    private String notifyUrl;
    @Field("name")
    private String name;
    @Field("money")
    private String money;
    @Field("total_fee")
    private String totalFee;
    @Field("notify_state")
    private int notifyState;
    @Field("state")
    private long state =0;
    @Field("add_time")
    private long addTime;
    @Field("update_time")
    private long updateTime;
    public Order() {
        this.id = ToolsUtil.getToken();
    }
}
