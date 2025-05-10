package com.example.demo.common.config.server;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author martix
 * @description
 * @time 2025/5/8 9:47
 */
@Configuration
@RequiredArgsConstructor
public class AppServerConfig {
    private final AppServerConfigProperties properties;

    // 添加额外的http端口
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            Connector connector = new Connector();
            connector.setPort(properties.getHttpPort());
            factory.addAdditionalTomcatConnectors(connector);
        };
    }
}
