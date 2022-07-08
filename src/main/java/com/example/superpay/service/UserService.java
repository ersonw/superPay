package com.example.superpay.service;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.dao.AdminDao;
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
    @Autowired
    private AdminDao adminDao;

    public ResponseData login(String username, String password, String ip) {
        log.error("ip address:{}, username:{}, password:{}", ip, username,password);
        User user = userRepository.findAllByUsername(username);
        if(user == null ) return  ResponseData.error("用户账号密码错误!");
        if(!adminDao.isIpAddress(user.getId(), ip)) return  ResponseData.error("IP 地址不在白名单内！");
        if(user.getState() == -1) return  ResponseData.error("用户状态异常，请联系管理员");
        if(!verifyPassword(user.getPassword(),password,user.getSalt())) return ResponseData.error("用户账号密码错误!");
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

    public ResponseData info(User user, String ip) {
        if (user == null) return ResponseData.error();
        User u = userRepository.findAllById(user.getId());
        if (u == null) return ResponseData.error();
        if(!adminDao.isIpAddress(u.getId(), ip)) return  ResponseData.error("IP 地址不在白名单内！");
        if( u.getState() != 0) return ResponseData.error("用户状态异常，请联系管理员!");
        u.setToken(user.getToken());
        authDao.pushUser(u);
        JSONObject object = ResponseData.object("username", u.getUsername());
        object.put("token", u.getToken());
        object.put("id", u.getId());
        object.put("admin", u.isAdmin());
        object.put("avatar", u.getAvatar());
        object.put("callbackUrl", u.getCallbackUrl());
        object.put("notifyUrl", u.getNotifyUrl());
        return ResponseData.success(object);
    }

    public ResponseData logout(User user, String ip) {
        if (user == null) return ResponseData.error();
        log.error("logout user name:{} ip:{}",user.getUsername(),ip);
        if(adminDao.isIpAddress(user.getId(), ip)) authDao.removeUser(user);
        return ResponseData.success("注销登录成功！");
    }

    public void test() {
        String s = "{\"id\":\"8045ca21MnP21b0ZFF44e3IeM9239kkQc2f9f308242egYp\",\"username\":\"admin\",\"password\":\"bc2c48951bd283c8324231fb8a8406b5\",\"salt\":\"HpDl52vZDIgoGH3NFZW8Xs5WZKRe3R6v\",\"secretKey\":\"3gOVsdBIgJdDSvOVhd8IlNgSMv43yfEk\",\"callbackUrl\":\"https://pay.telebott.com//api/adapter/epayOrder\",\"notifyUrl\":\"https://pay.telebott.com//api/adapter/epayOrder\",\"rate\":100,\"fee\":1,\"state\":0,\"addTime\":1655993107551,\"updateTime\":null,\"token\":\"817ee0b8kx8a6b1j8y4223Vd18726e2xe00359513f2172S\"}";
        User user = JSONObject.toJavaObject(JSONObject.parseObject(s),User.class);
//        userRepository.save(user);
    }

    public ResponseData userPassword(String oldValue, String newValue, User user, String ip) {
        if (user == null) return ResponseData.error("用户未登录或者已过期，请重新登录后操作!");
        if(StringUtils.isEmpty(oldValue)) return ResponseData.error("旧密码不可为空！");
        if (StringUtils.isEmpty(newValue)) return ResponseData.error("新密码不可为空！");
        MD5Util md5Util = new MD5Util(user.getSalt());
        String oldPassword = md5Util.getPassWord(oldValue);
        if(!oldPassword.equals(user.getPassword())) return ResponseData.error("旧密码验证错误！");
        String newPassword = md5Util.getPassWord(newValue);
        if(newPassword.equals(user.getPassword())) return ResponseData.error("旧密码与新密码一致！");
        user.setPassword(newPassword);
        userRepository.save(user);
        authDao.pushUser(user);
        return ResponseData.success(ResponseData.object("state",true));
    }

    public ResponseData details(User user, String ip, String serverName, int serverPort, String url) {
        if (user == null) return ResponseData.error();
        /**
         * 获取商户信息
         */
        JSONObject object = ResponseData.object("Pid", user.getPid());
        object.put("SecretKey",user.getSecretKey());
        /**
         * 获取支付网关
         */
        StringBuilder sb = new StringBuilder();
        if (url.startsWith("https")){
            sb.append("https://").append(serverName);
        }else {
            sb.append("http://").append(serverName);
        }
        if(serverPort != 80){
            sb.append(":").append(serverPort);
        }
//        object.put("网关地址",sb+"/");
        object.put("Documentation",sb+"/swagger-ui.html");
        sb.append("/v3api/Polymer");
        object.put("EPayUrl",sb.toString());
        object.put("CallbackUrl",user.getCallbackUrl());
        object.put("NotifyUrl",user.getNotifyUrl());
        return ResponseData.success(object);
    }

    public ResponseData detailsChange(String callbackUrl, String notifyUrl, User user, String ip) {
        if (user == null) return ResponseData.error();
        if(!callbackUrl.startsWith("http")) return ResponseData.error("callbackUrl must start with http:// or https://");
        if(!notifyUrl.startsWith("http")) return ResponseData.error("notifyUrl must start with http:// or https://");
        user.setCallbackUrl(callbackUrl);
        user.setNotifyUrl(notifyUrl);
        userRepository.save(user);
        authDao.pushUser(user);
        return ResponseData.success(ResponseData.object("state",true));
    }
}
