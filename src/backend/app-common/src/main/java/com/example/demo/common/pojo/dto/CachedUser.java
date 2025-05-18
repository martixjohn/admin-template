package com.example.demo.common.pojo.dto;

import lombok.Data;

import java.util.LinkedList;

/**
 * 用户缓存
 *
 * @author martix
 * @description 存储在Redis
 * @time 5/11/25 2:47 PM
 */
@Data
public class CachedUser {
    // 用户信息,脱敏
    private User user;
    // jwt token，长度为会话数量
    private LinkedList<String> tokens;
}
