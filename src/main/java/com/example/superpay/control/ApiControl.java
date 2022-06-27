package com.example.superpay.control;

import com.example.superpay.data.ResponseData;
import com.example.superpay.entity.User;
import com.example.superpay.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
