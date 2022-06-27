package com.example.superpay.control;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.config.ApiGlobalModel;
import com.example.superpay.data.*;
import com.example.superpay.service.AdapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "/v3api", tags = "下单网关")
@RestController
//@RequestMapping("/api/adapter")
public class AdapterControl {
    @Autowired
    private AdapterService service;
    @ApiIgnore
    @PostMapping("/v3api/notify")
    public String sNotify(@ModelAttribute ToPayNotify toPayNotify){
        return service.sNotify(toPayNotify);
    }
    @ApiIgnore
    @GetMapping("/v3api/ePayNotify")
    public String ePayNotify(@ModelAttribute EPayNotify ePayNotify){
        return service.ePayNotify(ePayNotify);
    }

}
