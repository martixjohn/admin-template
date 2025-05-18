package com.example.demo.common.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author martix
 * @description
 * @time 2025/4/23 10:09
 */
@Getter
@Setter
public class BasePO implements Serializable {

    // 物理键
    @TableId(type = IdType.AUTO)
    protected Long id;

//    // 逻辑主键
//    @TableField(fill = FieldFill.INSERT)
//    protected UUID uuid;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BasePO basePO = (BasePO) o;
        return Objects.equals(id, basePO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
