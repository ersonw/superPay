package com.example.superpay.control;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.config.ApiGlobalModel;
import com.example.superpay.data.ResponseData;
import com.example.superpay.data.pData;
import com.example.superpay.entity.User;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.service.UserService;
import com.example.superpay.util.MD5Util;
import com.example.superpay.util.MongoAutoidUtil;
import com.example.superpay.util.ToolsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

//@Api(value = "/api/user", tags = "用户接口")
@ApiResponses({
        @ApiResponse(code = 200, message = "成功"),
        @ApiResponse(code = 105, message = "未带token请求"),
        @ApiResponse(code = 106, message = "token非法或者登录已过期"),
})
@RestController
@RequestMapping("/api/user")
public class UserControl {
    @Autowired
    private UserService service;

    @PostMapping("/password")
    @ApiGlobalModel(component = pData.class, value = "oldValue,newValue")
    public ResponseData userPassword(@RequestBody pData data){
        return service.userPassword(data.getOldValue(),data.getNewValue(),data.getUser(), data.getIp());
    }
    @PostMapping("/login")
    @ApiGlobalModel(component = pData.class, value = "username,password")
    public ResponseData login(@RequestBody pData data){
        if (StringUtils.isEmpty(data.getUsername()) || StringUtils.isEmpty(data.getPassword())){
            return ResponseData.error("用户名或密码必填!");
        }
        return service.login(data.getUsername(),data.getPassword(), data.getIp());
    }
    @GetMapping("/info")
    public ResponseData info(@RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                             @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.info(User.getUser(user),ip);
    }
    @GetMapping("/logout")
    public ResponseData logout(@RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user,
                             @RequestParam(value = "ip") @ApiParam(hidden = true) String ip){
        return service.logout(User.getUser(user),ip);
    }
    @GetMapping("/test")
//    @ApiGlobalModel(component = pData.class, value = "toId,id,text,seek")
    public ResponseData test(
//            @PathVariable long id,
                                      @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user
//            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip
    ) {
        service.test();
        return ResponseData.success(user);
    }
}
