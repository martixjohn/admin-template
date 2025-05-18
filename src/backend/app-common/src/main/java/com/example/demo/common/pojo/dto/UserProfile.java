package com.example.demo.common.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户的个人信息，安全信息
 *
 * @author martix
 * @description 安全信息
 * @time 2025/5/9 16:45
 */
@Getter
@Setter
public class UserProfile implements Serializable {
    // 用户名，无法更改
    private String username;

    // 昵称
    private String nickname;

    // 电子邮件
    private String email;

    // 头像，本地存储路径
    private String avatarLocalPath;

    // 头像，服务器存储路径，IP:port/xxx
    private String avatarServerPath;
}
