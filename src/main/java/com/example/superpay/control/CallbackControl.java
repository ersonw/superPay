package com.example.superpay.control;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.data.*;
import com.example.superpay.service.AdapterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@Api(value = "/v3api", tags = "下单网关")
@Controller
public class CallbackControl {
    @Autowired
    private AdapterService service;

    @PostMapping("/v3api/Polymer")
    public ModelAndView ePayOrder(@ModelAttribute EPayData ePay){
        return service.ePayOrder(ePay);
    }

    @GetMapping("/v3api/test")
    public ModelAndView test(){
        return service.ePayOrder(service.test());
    }
    @ApiIgnore
    @GetMapping("/v3api/alipay")
    public ModelAndView alipay(@ModelAttribute AlipayNotifyParam param, HttpServletRequest req){
//        System.out.printf(ePayNotify+"\n");
        return service.alipay(param,req);
    }
    @ApiIgnore
    @GetMapping("/v3api/ePayReturn")
    public ModelAndView ePayReturn(@ModelAttribute EPayNotify ePayNotify){
//        System.out.printf(ePayNotify+"\n");
        return service.ePayReturn(ePayNotify);
    }
    @ApiIgnore
    @GetMapping("/v3api/testReturn")
    public ModelAndView testReturn(@ModelAttribute EPayNotify ePayNotify){
//        System.out.printf(ePayNotify+"\n");
        return service.testReturn(ePayNotify);
    }
    @ApiIgnore
    @GetMapping("/v3api/callback")
//    public ModelAndView sCallback(@ModelAttribute JSONObject jsonObject){
    public ModelAndView sCallback(@RequestParam(value = "outTradeNo") String out_trade_no,
                                  @RequestParam(value = "ip") String ip){
//        System.out.printf(jsonObject.toJSONString());
        return service.sCallback(out_trade_no,ip);
//        return new ModelAndView();
    }
    @ApiIgnore
    @GetMapping("/v3api/error")
    public ModelAndView sError(@ModelAttribute ToPayNotify toPayNotify){
        System.out.printf(toPayNotify.toString());
        return service.sError();
    }

}
