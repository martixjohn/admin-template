package com.example.demo.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户登录请求
 *
 * @author martix
 * @description
 * @time 2025/4/27 19:29
 */
public record UserLoginRequest(
        @Schema(title = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
        @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
