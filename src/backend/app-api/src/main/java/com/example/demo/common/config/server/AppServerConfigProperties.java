package com.example.demo.common.config.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author martix
 * @description
 * @time 2025/5/8 9:50
 */
@Component
@ConfigurationProperties("app.server")
@Data
public class AppServerConfigProperties {
    private String host = "0.0.0.0";
    private int httpPort = 80;
    private int httpsPort = 443;
}
