package com.example.demo.api.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户脱敏后的信息
 * @author martix
 * @description
 * @time 2025/4/26
 */
@Data
public class UserSafeVO {
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @Schema(description = "电子邮件", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "头像访问路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String avatar;

    @Schema(description = "上一次登录时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime lastLoginTime;

    @Schema(description = "用户创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdTime;

    @Schema(description = "用户数据更新时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime updatedTime;

    @Schema(description = "角色", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> roles;

    @Schema(description = "权限", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> permissions;
}
