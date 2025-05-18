package com.example.demo.common.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 安全相关配置文件
 *
 * @author martix
 * @description
 * @time 2025/4/22
 */
@ConfigurationProperties(prefix = "app.security")
@Component
@Data
public class AppSecurityConfigProperties {

    /**
     * 完全放开的URI，Spring Security不会进行拦截
     * 格式：METHOD URI或者URI
     */
    private List<String> whiteListUri = new ArrayList<>();


    private Authorization authorization = new Authorization();

    @Data
    public static class Authorization {
        /**
         * 可访问接口的uri
         * 支持Ant匹配和精确匹配
         * 格式1：请求方法 uri
         * GET /app/**
         * 格式2：uri
         * /hello/*
         *
         * @description 建议：优先使用 @PermitAllAuthentication进行精细控制，此项只用于内置框架匹配如swagger
         */
        private List<String> permitAllUri = new ArrayList<>(0);

        /**
         * 仅匿名访问接口的uri
         * 支持Ant匹配和精确匹配
         * 格式1：请求方法 uri
         * GET /app/**
         * 格式2：uri
         * /hello/*
         *
         * @description 建议：优先使用 @AnonymousAuthentication进行精细控制，此项只用于内置框架匹配如swagger
         */
        private List<String> anonymousUri = new ArrayList<>(0);
    }


    /**
     * 用户账户相关
     */
    private Account account = new Account();

    @Data
    public static class Account {

        /**
         * 用户名正则
         */
        private String usernamePattern = "[a-zA-Z0-9_-]{5,20}";

        /**
         * 密码正则
         */
        private String passwordPattern = "[a-zA-Z0-9_-]{5,20}";


    }

    private Session session = new Session();

    @Data
    public static class Session {

        /**
         * 会话过期时间
         */
        private Duration expiresIn = Duration.ofMinutes(30);

        /**
         * 用户最大会话数量，超过会导致旧会话失效
         */
        private int maximumSessions = 1;
    }

    private Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        /**
         * 服务器签名和验签私钥
         */
        private String secret = "XNIUGEf89dfncao1&ads";
    }
}
