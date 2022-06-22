package com.example.superpay;

import com.example.superpay.entity.User;
import com.example.superpay.repository.UserRepository;
import com.example.superpay.util.MongoAutoidUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class SuperPayApplicationTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoAutoidUtil mongoAutoidUtil;
    @Autowired
    private UserRepository userRepository;
    @Test
    void contextLoads() {
    }
    @Test
    public void add() {
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
//            userRepository.save()
        }

        Iterable<User> articles = userRepository.findAll();
        articles.forEach(article2 -> {
            System.out.println(article2.toString());
        });
    }
}
