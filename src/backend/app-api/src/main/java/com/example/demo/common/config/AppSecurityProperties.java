package com.example.demo.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限配置文件
 *
 * @author martix
 * @description
 * @time 2025/4/22 22:31
 */
@ConfigurationProperties(prefix = "app.security")
@Component
@Data
public class AppSecurityProperties {
    /**
     * 可访问接口的uri，建议：优先使用 @PermitAllAuthentication进行精细控制，此项只用于内置框架匹配如swagger
     * 支持Ant匹配和精确匹配
     * 格式1：请求方法 uri
     * GET /app/**
     * 格式2：uri
     * /hello/*
     */
    private List<String> permitAllUri = new ArrayList<>(0);

    /**
     * 仅匿名访问接口的uri，建议：优先使用 @AnonymousAuthentication进行精细控制，此项只用于内置框架匹配如swagger
     * 支持Ant匹配和精确匹配
     * 格式1：请求方法 uri
     * GET /app/**
     * 格式2：uri
     * /hello/*
     */
    private List<String> anonymousUri = new ArrayList<>(0);

//    /**
//     * token/jwt 密钥
//     */
//    private String tokenSecret = "1dfvh29834nvfhf0asif-f&";
//
//    /**
//     * token/jwt 签发者
//     */
//    private String tokenIssuer = "123";



    /**
     * 用户账户相关
     */
    private Account account;

    @Component
    @ConfigurationProperties(prefix = "app.security.account")
    @Data
    public static class Account {

        /**
         * 用户名正则
         */
        private String usernamePattern = "[a-zA-Z0-9_-]{6,10}";

        /**
         * 密码正则
         */
        private String passwordPattern = "[a-zA-Z0-9_-]{6,10}";

        /**
         * 登录过期时间，单位秒
         */
        private long expiresIn = 3600;
    }
}
