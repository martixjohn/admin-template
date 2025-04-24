package com.example.demo.common.config.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:44
 */
@Configuration
@MapperScan("com.example.demo.repository")
public class MybatisPlusConfig {

}
