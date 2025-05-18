package com.example.demo.common.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * RedisCache
 * 不支持事务
 *
 * @author martix
 * @description
 * @time 5/11/25 2:21 PM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheManager {

    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> objectRedisTemplate;

    public <T> void rightPushInList(String key, T value) {
        objectRedisTemplate.opsForList().rightPush(key, value);
    }

    public <T> T leftPopInList(String key, Class<T> returnClazz) {
        String val = stringRedisTemplate.opsForList().leftPop(key);
        return deserialize(returnClazz, val);
    }

    public String leftPopInList(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    public void increment(String key, long delta) {
        stringRedisTemplate.opsForValue().increment(key, delta);
    }

    // 设置缓存,并指定过期时间
    public <T> void set(String key, T value, Duration duration) {
        objectRedisTemplate.opsForValue().set(key, value, duration);
    }

    // 获取缓存, 没有返回null
    @Nullable
    public <T> T get(String key, Class<T> returnClazz) {
        String val = stringRedisTemplate.opsForValue().get(key);
        return deserialize(returnClazz, val);
    }

    @Nullable
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 是否有缓存
    public boolean has(String key) {
        return objectRedisTemplate.hasKey(key);
    }

    // 设置过期时间
    public void expire(String key, long timeout, TimeUnit unit) {
        objectRedisTemplate.expire(key, timeout, unit);
    }

    // 获得剩余过期时间
    public Duration getExpire(String key) {
        return Duration.ofMillis(objectRedisTemplate.getExpire(key, TimeUnit.MILLISECONDS));
    }

    // 删除缓存
    public void delete(String key) {
        objectRedisTemplate.delete(key);
    }

    // 反序列化
    private <T> T deserialize(Class<T> clazz, String val) {
        if (val == null) {
            return null;
        }
        if (clazz == String.class) {
            @SuppressWarnings("unchecked")
            T t = (T) val;
            return t;
        }
        try {
            return objectMapper.readValue(val, clazz);
        } catch (JsonProcessingException e) {
            log.warn("redis反序列化失败", e);
            throw new RuntimeException(e);
        }
    }

}
