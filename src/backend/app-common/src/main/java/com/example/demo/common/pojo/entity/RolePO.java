package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色
 * @author martix
 * @description
 * @time 2025/4/23 10:07
 */
@Getter
@Setter
@ToString
@TableName("role")
public class RolePO extends BasePO {

    // 角色名，对应Spring Security
    private String name;

}
