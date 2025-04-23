package com.example.demo.api.common.annotation;

import java.lang.annotation.*;

/**
 * 允许所有
 * @author martix
 * @description
 * @time 2025/4/23 16:35
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitAllAuthentication {

}
