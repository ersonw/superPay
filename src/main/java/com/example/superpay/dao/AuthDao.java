package com.example.superpay.dao;


import com.alibaba.fastjson.JSONObject;
import com.example.superpay.entity.User;
import com.example.superpay.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AuthDao {
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Timer timer = new Timer();

    public void pushUser(User userToken){
        if (StringUtils.isNotEmpty(userToken.getToken())) {
            Set users = redisTemplate.opsForSet().members("user");
            assert users != null;
//        System.out.println(users.toString());
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user, User.class);
                if (userEntity.getToken().equals(userToken.getToken()) || Objects.equals(userEntity.getId(), userToken.getId())){
                    popUser(userEntity);
                }
            }
            redisTemplate.opsForSet().add("user",userToken);
        }
    }
    public void removeUser(User userToken){
        Set users = redisTemplate.opsForSet().members("user");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user,User.class);
                if (userEntity.getToken().equals(userToken.getToken())){
                    popUser(userEntity);
                }
            }
        }
    }
    public void popUser(User userToken){
        redisTemplate.opsForSet().remove("user" ,userToken);
    }
    public User findUserByToken(String token) {
        Set users = redisTemplate.opsForSet().members("user");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user,User.class);
                if (userEntity.getToken().equals(token)){
                    return userEntity;
                }
            }
        }
        return null;
    }
    public User findUserByUserId(String userId) {
        Set users = redisTemplate.opsForSet().members("user");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                User userEntity = objectMapper.convertValue(user,User.class);
                if (Objects.equals(userEntity.getId(), userId)){
                    return userEntity;
                }
            }
        }
        return null;
    }

    public Set getAllUser(){
        return redisTemplate.opsForSet().members("user");
    }
}
