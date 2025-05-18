package com.example.demo.common.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 细分权限
 * @author martix
 * @description
 * @time 2025/4/23 10:27
 */
@Getter
@Setter
@ToString
@TableName("permission")
public class PermissionPO extends BasePO {
    // 权限名，对应Spring Security
    private String name;
}
