package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
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
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdTime;

    // 数据更新时间
    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updatedTime;

    // 是否删除（软删除）
    @TableLogic
    protected Boolean deleted;

}
