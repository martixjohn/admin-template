package com.example.demo.common.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 和持久层绑定的DTO基类
 * @author martix
 * @description
 * @time 2025/4/26
 */
@Slf4j
@Getter
@Setter
public class BaseDTO implements Serializable {

    // 对应数据库id
    protected Long id;

//    // 对应数据库id
//    protected UUID uuid;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseDTO baseDTO = (BaseDTO) o;
        log.info("baseDTO.equals: {}", baseDTO);
        return Objects.equals(id, baseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
