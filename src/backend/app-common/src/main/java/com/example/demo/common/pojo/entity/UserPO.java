package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author martix
 * @description
 * @time 2025/4/21 22:00
 */
@Getter
@Setter
@ToString
@TableName("user")
public class UserPO extends BasePO {
    // 用户名
    private String username;

    // 密码，hash加密后的
    private String password;

    // 电子邮件
    private String email;

    // 头像，本地存储路径
    private String avatarLocalPath;

    // 头像，服务器存储路径，IP:port/xxx
    private String avatarServerPath;

    // 用户是否被封锁
    private Boolean accountLocked = false;

    // 用户是否被禁用
    private Boolean disabled = false;

    // 上一次登录时间
    private LocalDateTime lastLoginTime;
}
