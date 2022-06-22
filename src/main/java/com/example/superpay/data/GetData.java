package com.example.superpay.data;


import com.alibaba.fastjson.JSONObject;
import com.example.superpay.entity.User;
import io.swagger.annotations.ApiModelProperty;

public class GetData {
    @ApiModelProperty(value = "page",name = "分页",required = false)
    private Integer page;
    @ApiModelProperty(value = "limit",name = "每页显示数量",required = false)
    private Integer limit;
    @ApiModelProperty(hidden = true)
    private String user;
    public User getUser() {
        JSONObject jsonObject = JSONObject.parseObject(user);
        return JSONObject.toJavaObject(jsonObject, User.class);
    }
}
