package com.example.demo.api.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户脱敏后的信息
 * @author martix
 * @description
 * @time 2025/4/26
 */
@Data
public class UserSafeVO {
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "电子邮件")
    private String email;

    @Schema(description = "头像访问路径")
    private String avatarServerPath;

    @Schema(description = "上一次登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "用户创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "用户数据更新时间")
    private LocalDateTime updatedTime;
}
