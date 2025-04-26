package com.example.demo.common.pojo.service;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author martix
 * @description
 * @time 2025/4/26
 */
@Data
public class BaseDTO {

    // 对应数据库id
    protected Long id;

    // 数据创建时间
    protected LocalDateTime createdTime;

    // 数据更新时间
    protected LocalDateTime updatedTime;

    // 描述信息
    protected String description;

    // 创建者
    protected Long createdBy;

    // 更新者
    protected Long updatedBy;
}
