package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 细分权限
 * @author martix
 * @description
 * @time 2025/4/23 10:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("permission")
public class PermissionPO extends BasePO {
    // 权限名，对应Spring Security
    private String name;
}
