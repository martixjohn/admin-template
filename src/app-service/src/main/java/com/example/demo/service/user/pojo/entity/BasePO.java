package com.example.demo.user.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author martix
 * @description
 * @time 2025/4/23 10:09
 */
@Data
public class BasePO {
    @TableId(type = IdType.AUTO)
    protected Long id;

    // 数据创建时间
    protected LocalDateTime createdTime;

    // 数据更新时间
    protected LocalDateTime updatedTime;

    // 数据删除时间（软删除）
    protected LocalDateTime deletedTime;

    // 是否删除（软删除）
    protected Boolean deleted;

}
