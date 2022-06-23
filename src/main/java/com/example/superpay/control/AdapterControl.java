package com.example.superpay.control;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.config.ApiGlobalModel;
import com.example.superpay.data.EPayData;
import com.example.superpay.data.ResponseData;
import com.example.superpay.data.pData;
import com.example.superpay.service.AdapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/api/adapter", tags = "下单网关")
@RestController
@RequestMapping("/api/adapter")
public class AdapterControl {
    @Autowired
    private AdapterService service;
//    @ApiIgnore
//    @PostMapping("/epayOrder")
//    @ApiGlobalModel(component = EPayData.class, value = "toId,id,text,seek")
//    public ResponseData epayOrder(@RequestBody EPayData epay){
//        return ResponseData.success();
//    }
    @PostMapping("/epayOrder")
    public ResponseData ePayOrder(@ModelAttribute EPayData ePay){
//        System.out.printf(epay.toString()+"\n");
        return service.ePayOrder(ePay);
    }
    @GetMapping("/epayOrder")
    public String ePayOrder(@ModelAttribute JSONObject json){
        return "ok";
    }
}
