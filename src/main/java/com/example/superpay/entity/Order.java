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
    private String trade_no;
    @Field("out_trade_no")
    private String out_trade_no;
    @Field("type_id")
    private String type_id;
    @Field("ip")
    private String ip;
    @Field("return_url")
    private String return_url;
    @Field("notify_url")
    private String notify_url;
    @Field("name")
    private String name;
    @Field("money")
    private String money;
    @Field("trade_status")
    private String trade_status;
    @Field("add_time")
    private long add_time;
    @Field("update_time")
    private long update_time;
    public Order() {
        this.id = ToolsUtil.getToken();
    }
}
