package com.example.superpay.config;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;

/**
 * reactive redis
 * @Author:wangqipeng
 * @Date:14:38 2019-07-03
 */

@Configuration
public class RedisDatasourceConfiguration {

    @Value("${redis.isCleanRedisCache:false}")
    private String cleanRedisCache;
    @Value("${redis.host:127.0.0.1}")
    public String host;
    @Value("${redis.port:6379}")
    public Integer port;
    private String password;

    @Value("${redis.timeout:2000}")
    public Integer timeout;
    public Integer maxIdle = 16;
    public Integer minIdle = 5;
    public Integer maxTotal = 30;


    @Bean
    public RedisTemplate<String, String> stringRedisTemplate1() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setDatabase(1);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        return createRedisTemplate(creatFactory(configuration));
    }

    @Bean
    public RedisTemplate<String, String> stringRedisTemplate2() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setDatabase(2);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        return createRedisTemplate(creatFactory(configuration));
    }
    @Bean
    public RedisTemplate<String, String> stringRedisTemplate3() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setDatabase(3);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        return createRedisTemplate(creatFactory(configuration));
    }
    @Bean
    public RedisTemplate<String, String> stringRedisTemplate4() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setDatabase(4);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        return createRedisTemplate(creatFactory(configuration));
    }

    @Bean
    public RedisTemplate<String, String> stringRedisTemplate5() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setDatabase(5);
        if (!ObjectUtils.isEmpty(password)) {
            RedisPassword redisPassword = RedisPassword.of(password);
            configuration.setPassword(redisPassword);
        }
        return createRedisTemplate(creatFactory(configuration));
    }

    private RedisTemplate<String, String> getSerializerRedisTemplate(){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    private RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = getSerializerRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    private GenericObjectPoolConfig getGenericObjectPoolConfig(){
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxTotal);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(timeout);
        return genericObjectPoolConfig;
    }

    private LettuceConnectionFactory creatFactory(RedisStandaloneConfiguration configuration){
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.poolConfig(getGenericObjectPoolConfig());

//        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();
//        builder.clientResources(clientResources());
//        builder.commandTimeout(Duration.ofSeconds(3000));
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, builder.build());
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }
}
