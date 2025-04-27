package com.example.demo.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author martix
 * @description
 * @time 2025/4/23 10:09
 */
@Data
public class BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    protected Long id;

    // 数据创建时间
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createdTime;

    // 数据更新时间，最后软删除也会更新
    @TableField(fill = FieldFill.UPDATE)
    protected LocalDateTime updatedTime;


    // 是否删除（软删除）
    @TableLogic(value = "0", delval = "1")
    protected Boolean deleted = false;

    // 描述信息
    protected String description;

    // 创建者
    protected Long createdBy;

    // 更新者
    protected Long updatedBy;
}
