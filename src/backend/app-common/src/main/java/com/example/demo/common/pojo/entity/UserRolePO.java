package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户权限关联表
 *
 * @author martix
 * @description 关联表
 * @time 2025/4/23 11:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_role")
public class UserRolePO extends BasePO {
    public Long userId;
    public Long roleId;
}
