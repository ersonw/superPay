package com.example.superpay.control;

import com.example.superpay.config.ApiGlobalModel;
import com.example.superpay.data.ResponseData;
import com.example.superpay.data.pData;
import com.example.superpay.entity.User;
import com.example.superpay.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "/api", tags = "管理后台")
@RestController
@RequestMapping("/api")
public class ApiControl {
    @Autowired
    private ApiService service;

    @GetMapping("/dashboard")
    public ResponseData dashboard(@RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                                  @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.dashboard(User.getUser(user),ip);
    }
    @GetMapping("/dayData")
    public ResponseData dayData(@RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                                  @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.dayData(User.getUser(user),ip);
    }
    @GetMapping("/orders/{page}")
    public ResponseData orders(@PathVariable("page") int page,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                                  @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.orders(page,User.getUser(user),ip);
    }
    @PostMapping("/orderNotify")
    @ApiGlobalModel(component = pData.class, value = "selected")
    public ResponseData orderNotify(@RequestBody pData data){
        return service.orderNotify(data.getSelected(),data.getUser(),data.getIp());
    }
    @PostMapping("/orderDelete")
    @ApiGlobalModel(component = pData.class, value = "selected")
    public ResponseData orderDelete(@RequestBody pData data){
        return service.orderDelete(data.getSelected(),data.getUser(),data.getIp());
    }
    @GetMapping("/orders/{page}/{orderNo}")
    public ResponseData orderNo(@PathVariable("page") int page,
                                @PathVariable("orderNo") String orderNo,
            @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                                  @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.orderNo(page,orderNo,User.getUser(user),ip);
    }
    @GetMapping("/orderDetails/{id}")
    public ResponseData orderDetails(@PathVariable("id") String orderNo,
                                     @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                                  @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.dayData(User.getUser(user),ip);
    }
    @GetMapping("/merchantDetails")
    public ResponseData merchantDetails(@RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                                  @RequestParam(value = "ip") @ApiParam(hidden = true) String ip,
                                  @RequestParam(value = "url",required = false) @ApiParam(hidden = true) String url,
                                  @RequestParam(value = "serverName",required = false) @ApiParam(hidden = true) String serverName,
                                  @RequestParam(value = "serverPort",defaultValue = "80") @ApiParam(hidden = true) int serverPort
                                        ){
        return service.merchantDetails(User.getUser(user),ip,serverName,serverPort,url);
    }
}
