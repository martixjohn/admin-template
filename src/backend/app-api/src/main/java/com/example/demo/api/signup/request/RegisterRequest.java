package com.example.demo.api.signup.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户登录请求
 *
 * @author martix
 * @description
 * @time 2025/4/24 14:50
 */

public record RegisterRequest(
        @Schema(description = "用户名")
        String username,
        @Schema(description = "密码")
        String password
) {

}