package com.example.demo.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 用作测试
 * @author martix
 * @description
 * @time 2025/4/24 14:22
 */
@Component
@Slf4j
public class ApplicationTestAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CharacterEncodingFilter bean = applicationContext.getBean(CharacterEncodingFilter.class);
        String encoding = bean.getEncoding();
        log.info("encoding: {}", encoding);
    }
}
