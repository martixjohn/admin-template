package com.example.demo.common.auth;

/**
 * 权限相关
 *
 * @author martix
 * @description
 * @time 2025/4/24
 */
public abstract class Authorities {
    /**
     * 超级管理员，通常作为开发者或运维
     */
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    /**
     * 管理员
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * 普通用户
     */
    public static final String ROLE_USER = "ROLE_USER";

}
