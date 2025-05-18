package com.example.demo.common.manager;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.example.demo.common.config.security.AppSecurityConfigProperties;
import com.example.demo.common.pojo.dto.User;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author martix
 * @description
 * @time 5/11/25 1:54 PM
 */
@Slf4j
@Service
public class JwtManager {

    private byte[] secret;

//    private long expiresInMillis;

    @Autowired
    private void setAppSecurityConfigProperties(AppSecurityConfigProperties appSecurityConfigProperties) {
        secret = appSecurityConfigProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
//        Duration expiresIn = appSecurityConfigProperties.getSession().getExpiresIn();
//        expiresInMillis = expiresIn.getSeconds() * 1000;
//        expiresInMillis += expiresIn.getNano() / 1_000_000;
//        log.info("设置的TOKEN过期时间: {}ms", expiresInMillis);
    }

    /**
     * 生成JWT Base64
     */
    public String generateToken(User user) {

        Map<String, Object> payload = new HashMap<>();
        long now = System.currentTimeMillis();
        payload.put(JWTPayload.SUBJECT, user.getUsername());
        payload.put(JWTPayload.ISSUED_AT, now);
//        payload.put(JWTPayload.EXPIRES_AT, now + expiresInMillis);

        // HS256(HmacSHA256)
        return JWTUtil.createToken(payload, secret);
    }

    /**
     * 验证是否有效
     *
     * @param token           Base64后的JWT token
     * @param validateExpired 是否验证过期时间
     * @return 用户名, 无效为null
     */
    @Nullable
    public String verifyAndGetUsername(String token, boolean validateExpired) {
        try {
            JWT jwt = JWT.of(token).setKey(secret);
            boolean ok = validateExpired ? jwt.validate(0) : jwt.verify();
            return ok ? (String) jwt.getPayload(JWTPayload.SUBJECT) : null;
        } catch (RuntimeException e) {
            return null;
        }
    }
}
