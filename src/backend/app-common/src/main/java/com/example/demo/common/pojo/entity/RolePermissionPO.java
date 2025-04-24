package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色_权限关联表
 *
 * @author martix
 * @description 关联表
 * @time 2025/4/23 11:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("role_permission")
public class RolePermissionPO extends BasePO {
    public Long roleId;
    public Long permissionId;
}
