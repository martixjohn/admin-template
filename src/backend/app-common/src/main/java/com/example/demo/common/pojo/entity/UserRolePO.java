package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户权限关联表
 *
 * @author martix
 * @description 关联表
 * @time 2025/4/23 11:21
 */
@Getter
@Setter
@ToString
@TableName("user_role")
public class UserRolePO extends BasePO {
    public Long userId;
    public Long roleId;
}
