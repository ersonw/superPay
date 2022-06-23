package com.example.superpay.util;

import com.example.superpay.dao.AuthDao;
import com.example.superpay.repository.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
public class Utils {
    private static Utils self;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void initPost(){
        self = this;
        System.out.printf("加载器成功！\n");
    }
    public static AuthDao getAuthDao(){
        return self.authDao;
    }
    public static UserRepository getUserRepository(){
        return self.userRepository;
    }
}
