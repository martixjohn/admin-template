package com.example.demo.common.config.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMVC配置
 * @author martix
 * @description
 * @time 2025/4/24 14:11
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {

        };
    }

    @Bean
    public ObjectMapper jsonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build();
    }

}
