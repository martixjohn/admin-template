package com.example.demo.common.config.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 全局通用配置
 * @author martix
 * @description
 * @time 2025/4/27 19:06
 */
@Component
@ConfigurationProperties("app.common")
@Data
public class AppCommonConfigProperties {
    private Charset charset = StandardCharsets.UTF_8;
}
