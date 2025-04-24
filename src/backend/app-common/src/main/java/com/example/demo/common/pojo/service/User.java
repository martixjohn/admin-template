package com.example.demo.common.pojo.service;

import lombok.Data;

import java.util.Set;

/**
 * @author martix
 * @description
 * @time 2025/4/23 11:01
 */
@Data
public class User {
    // 用户名
    private String username;

    // 密码，hash加密后的
    private String password;

    // 角色名称
    private Set<String> roles;

    // 细分权限名称
    private Set<String> permissions;

    // 电子邮件
    private String email;

    // 头像，本地存储路径
    private String avatarLocalPath;

    // 头像，服务器存储路径，IP:port/xxx
    private String avatarServerPath;
}
