package com.example.demo.common.config.app;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author martix
 * @description
 * @time 2025/4/22 22:28
 */
@Configuration
@EnableConfigurationProperties(AppSecurityProperties.class)
public class AppConfig {
}
