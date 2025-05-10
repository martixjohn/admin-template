package com.example.demo.common.config.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据库相关配置
 * @author martix
 * @description
 * @time 2025/4/26 16:24
 */
@Component
@ConfigurationProperties("app.db")
@Data
public class AppDbConfigProperties {
    /**
     * 启动程序时，是否初始化数据库
     */
    private boolean enableInit = false;

    /**
     * 超级管理员用户名，当enableInit为true才设置
     */
    private String superAdminUsername = "super_admin";

    /**
     * 超级管理员密码，当enableInit为true才设置
     */
    private String superAdminPassword = "super_admin";
}
