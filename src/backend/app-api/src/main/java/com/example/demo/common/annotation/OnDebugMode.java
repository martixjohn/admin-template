package com.example.demo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识仅在开发和测试时使用，非开发或测试环境使用抛出异常
 *
 * @author martix
 * @description
 * @time 2025/5/9 18:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface OnDebugMode {
}
