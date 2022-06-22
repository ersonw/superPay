package com.example.superpay.control;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.config.ApiGlobalModel;
import com.example.superpay.data.ResponseData;
import com.example.superpay.data.pData;
import com.example.superpay.entity.User;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.MongoAutoidUtil;
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
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoAutoidUtil mongoAutoidUtil;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    @ApiGlobalModel(component = pData.class, value = "toId,id,text,seek")
    public ResponseData commentDelete(
//            @PathVariable long id,
//                                      @RequestParam(value = "user",required = false) @ApiParam(hidden = true) String u ,
//                                      @RequestParam(value = "ip") @ApiParam(hidden = true) String ip
    ){

        for (int i = 0; i < 10; i++) {  //增加一条记录
            User user = new User();
            user.setAddTime(System.currentTimeMillis());
            user.setFee(1);
            user.setCallbackUrl("sdasd");
            user.setNotifyUrl("sdasd");
            user.setPassword("dsads");
            user.setUsername("dsads");
            user.setSalt("dsads");
            user.setRate(1);
            user.setState(1);
            user.setSecretKey("dasd");
//            user.setId(mongoAutoidUtil.getNextSequence("user"));
            mongoTemplate.save(user);
        }

//        Iterable<User> articles = userRepository.findAll();
//        articles.forEach(article2 -> {
//            System.out.println(article2.toString());
//        });
        return ResponseData.success();
    }
}
