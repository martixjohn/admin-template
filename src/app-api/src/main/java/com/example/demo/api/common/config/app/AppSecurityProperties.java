package com.example.demo.common.config.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author martix
 * @description
 * @time 2025/4/22 22:31
 */
@ConfigurationProperties(prefix = "app.security")
@Data
public class AppSecurityProperties {
//    private List<String> permitAllUri;

//    public List<String> anonymousUri;
}
