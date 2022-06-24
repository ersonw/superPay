package com.example.superpay.service;

import com.example.superpay.dao.AuthDao;
import com.example.superpay.data.ResponseData;
import com.example.superpay.entity.Login;
import com.example.superpay.entity.User;
import com.example.superpay.repository.LoginRepository;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.MD5Util;
import com.example.superpay.util.ToolsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private AuthDao authDao;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private UserRepository userRepository;

    public ResponseData login(String username, String password, String ip) {
        log.error("ip address:{}, username:{}, password:{}", ip, username,password);
        User user = userRepository.findAllByUsername(username);
        if(user == null) return  ResponseData.error("user not found");
//        System.out.printf(getPassword(password,user.getSalt()));
//        user.setPassword("bc2c48951bd283c8324231fb8a8406b5");
//        userRepository.save(user);
        if(!verifyPassword(user.getPassword(),password,user.getSalt())) return ResponseData.error("password is Blank!");
        user.setToken(ToolsUtil.getToken());
        authDao.pushUser(user);
        loginRepository.save(new Login(user.getId(),ip));
        return ResponseData.success(ResponseData.object("token",user.getToken()));
    }
    public boolean verifyPassword(String old,String pass,String salt){
        return getPassword(pass, salt).equals(old);
    }
    public String getPassword(String password, String salt){
        MD5Util md5Util = new MD5Util(salt);
        return md5Util.getPassWord(password);
    }
}
