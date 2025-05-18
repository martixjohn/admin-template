package com.example.demo.common.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author martix
 * @description
 * @time 5/11/25 2:22 PM
 */
@Configuration
public class RedisConfig {

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(redisValueSerializer());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(redisValueSerializer());
        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<String, Object> transactionalRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setValueSerializer(redisValueSerializer());
//        redisTemplate.setHashKeySerializer(RedisSerializer.string());
//        redisTemplate.setHashValueSerializer(redisValueSerializer());
//        return redisTemplate;
//    }

    public RedisSerializer<Object> redisValueSerializer() {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
