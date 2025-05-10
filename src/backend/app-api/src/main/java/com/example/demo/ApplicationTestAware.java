package com.example.demo;

import com.example.demo.common.config.security.AppSecurityConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
//        SessionInformationExpiredStrategy bean1 = applicationContext.getBean(SessionInformationExpiredStrategy.class);
//        log.info("bean1: {}", bean1);

        AppSecurityConfigProperties bean = applicationContext.getBean(AppSecurityConfigProperties.class);
        log.info("读取配置: {}", bean);

//        AbstractAuthenticationProcessingFilter filter = applicationContext.getBean(AbstractAuthenticationProcessingFilter.class);
//        log.info("AbstractAuthenticationProcessingFilter: {}", filter);
//        RememberMeAuthenticationFilter rememberMeAuthenticationFilter = applicationContext.getBean(RememberMeAuthenticationFilter.class);
//        log.info("remember-me authentication filter: {}", rememberMeAuthenticationFilter);
    }
}
