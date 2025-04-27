package com.example.demo.api.user.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author martix
 * @description
 * @time 2025/4/27 18:07
 */
public record UserChangePasswordRequest(
        @Schema(description = "旧密码")
        String oldPassword,
        @Schema(description = "新密码")
        String newPassword
) {
}
