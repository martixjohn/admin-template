package com.example.demo.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author martix
 * @description
 * @time 2025/4/27 19:29
 */
public record UserLoginRequest(
        @Schema(title="用户名")
        String username,
        @Schema(description = "密码")
        String password
) {
}
