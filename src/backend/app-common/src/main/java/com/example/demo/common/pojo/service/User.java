package com.example.demo.common.pojo.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author martix
 * @description
 * @time 2025/4/23 11:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDTO implements UserDetails {
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

    // 用户是否被封锁
    private Boolean accountLocked;

    // 用户是否被禁用
    private Boolean disabled;

    // 上一次登录时间
    private LocalDateTime lastLoginTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        LinkedList<String> strings = new LinkedList<>(roles);
        strings.addAll(permissions);
        return strings.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
