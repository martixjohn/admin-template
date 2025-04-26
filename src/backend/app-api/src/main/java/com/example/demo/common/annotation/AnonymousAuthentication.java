package com.example.demo.common.annotation;

import java.lang.annotation.*;

/**
 * 只允许匿名访问
 * 只用在Controller的HandlerMethod
 * @author martix
 * @description Spring Security配置
 * @time 2025/4/23 16:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AnonymousAuthentication {

}
