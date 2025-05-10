package com.example.demo.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户注册请求
 * @author martix
 * @description
 * @time 2025/4/27 17:13
 */
public record UserRegisterRequest(
        @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
        @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {
}
