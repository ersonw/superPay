package com.example.superpay.config;

import com.alibaba.fastjson.JSONObject;
import com.example.superpay.util.AESUtils;
import com.example.superpay.util.ToolsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RedisAnimal<T> {
    private final Timer timer = new Timer();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private Class<T> clazz;
    private String collectionName;
    public RedisAnimal(){}
    public RedisAnimal(Class<T> clazz){
        this.clazz = clazz;
        String[] collectionNames = clazz.getCanonicalName().split("\\.");
        this.collectionName = ToolsUtil.toLowerCaseFirstOne(collectionNames[collectionNames.length - 1]);
    }
    public List<T> findAll(){
        List<T> list = new ArrayList<>();
        Set<String> values = redisTemplate.opsForSet().members(this.collectionName);
        if (values != null) {
            for (String v: values) {
                if (StringUtils.isNotEmpty(v)){
                    String s = v;
                    String d = AESUtils.Decrypt(v);
                    if (StringUtils.isNotEmpty(d)) s = d;
                    list.add(JSONObject.toJavaObject(JSONObject.parseObject(s),this.clazz));
                }
            }
        }
        return list;
    }
    public T findAllByFirst(String key, Object value){
        List<T> list = findAllBy(key,value);
        if (list.size() == 0) return null;
        return list.get(0);
    }
    public T findAllByLast(String key, Object value){
        List<T> list = findAllBy(key,value);
        if (list.size() == 0) return null;
        return list.get(list.size() -1);
    }
    public List<T> findAllBy(String key, Object value){
        List<T> results = findAll();
        List<T> list = new ArrayList<>();
        for (T result: results) {
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(result));
            for (String k: object.keySet()) {
                if (k.equals(key)){
                    if (value instanceof String && value.equals(object.getString(k))){
                        list.add(result);
                    }else if (value == object.get(k)){
                        list.add(result);
                    }
                }
            }
        }
        return list;
    }
    public void deleteAllBy(String key, Object value){
        List<T> results = findAll();
        for (T result: results) {
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(result));
            for (String k: object.keySet()) {
                if (k.equals(key)){
                    if (value instanceof String && value.equals(object.getString(k))){
                        delete(result);
                    }else if (value == object.get(k)){
                        delete(result);
                    }
                }
            }
        }
    }
    public void delete(T value){
        if (value == null)return;
        Set<String> values = redisTemplate.opsForSet().members(this.collectionName);
        assert values != null;
        String e = JSONObject.toJSONString(value);
        e = AESUtils.Encrypt(e);
        for (String v: values) {
            if (StringUtils.isNotEmpty(v)){
                if (Objects.equals(e, v)) redisTemplate.opsForSet().remove(this.collectionName,v);
            }
        }
    }
    public void deleteAll(){
        redisTemplate.delete(this.collectionName);
    }
    public void save(T value,long seconds){
        save(value);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                delete(value);
            }
        }, 1000 * seconds);
    }
    public void save(T value){
        if (value == null)return;
        String e = JSONObject.toJSONString(value);
        redisTemplate.opsForSet().add(this.collectionName, AESUtils.Encrypt(e));
    }
    public void saveAll(List<T> values){
        for (T value: values) {
            save(value);
        }
    }
}
