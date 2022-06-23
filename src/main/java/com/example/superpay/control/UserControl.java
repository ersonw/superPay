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

@Api(value = "/api/user", tags = "用户接口")
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

    @PostMapping("/login")
    @ApiGlobalModel(component = pData.class, value = "username,password")
    public ResponseData login(@RequestBody pData data){
//        System.out.printf(data.toString());
//        System.out.printf("\n");
//        return ResponseData.success(data.getIp());
        if (StringUtils.isEmpty(data.getUsername()) || StringUtils.isEmpty(data.getPassword())){
            return ResponseData.error("用户名或密码必填!");
        }
        return service.login(data.getUsername(),data.getPassword(), data.getIp());
    }

    @GetMapping("/test")
//    @ApiGlobalModel(component = pData.class, value = "toId,id,text,seek")
    public ResponseData test(
//            @PathVariable long id,
                                      @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String user
//            @RequestParam(value = "ip") @ApiParam(hidden = true) String ip
    ) {
//        User user = new User();
//        user.setAddTime(System.currentTimeMillis());
//        user.setFee(1);
//        user.setCallbackUrl("https://pay.telebott.com//api/adapter/epayOrder");
//        user.setNotifyUrl("https://pay.telebott.com//api/adapter/epayOrder");
//        user.setPassword("3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk");
//        user.setUsername("admin");
//        user.setSalt("HpDl52vZDIgoGH3NFZW8Xs5WZKRe3R6v");
//        user.setSecretKey(user.getPassword());
//        mongoTemplate.save(user);
//        System.out.printf(user.getId());
//        9MjfGvQ0mhXS4JFjAl2owcGdUpXAh3He
//        MD5Util md5Util = new MD5Util(user.getSalt());
//        System.out.printf(md5Util.getPassWord(md5Util.getMD5(user.getPassword())));
        return ResponseData.success(user);
    }
}
