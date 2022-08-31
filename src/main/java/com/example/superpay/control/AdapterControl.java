package com.example.superpay.control;

import com.example.superpay.data.*;
import com.example.superpay.service.AdapterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

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

    @ApiIgnore
    @PostMapping("/v3api/alipayNotify")
    public String alipayNotify(@ModelAttribute AlipayNotifyParam param, HttpServletRequest req){
        return service.alipayNotify(param,req);
    }
    @ApiIgnore
    @PostMapping("/v3api/wxPayNotify")
    public String wxPayNotify(HttpServletRequest request){
        return service.wxPayNotify(request);
    }
}
