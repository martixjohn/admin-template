package com.example.demo.common.annotation;

import java.lang.annotation.*;

/**
 * 允许所有用户访问
 * @author martix
 * @description Spring Security配置
 * @time 2025/4/23 16:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitAllAuthentication {

}
