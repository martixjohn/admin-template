package com.example.demo.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import lombok.Data;

import java.util.List;

/**
 * 分页结果
 *
 * @author martix
 * @description
 * @time 5/12/25 9:52 AM
 */
@Data
public class PageVO<T> {

    @Schema(description = "数据库中总数", requiredMode = Schema.RequiredMode.REQUIRED)
    private long total;

    @Schema(description = "当前页，从1开始", requiredMode = Schema.RequiredMode.REQUIRED)
    private long current;

    @Schema(description = "页大小", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageSize;


    @Schema(description = "数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> records;

    public PageVO(long total, long current, long pageSize, @Nonnull List<T> records) {
        this.total = total;
        this.pageSize = pageSize;
        this.current = current;
        this.records = records;
    }
}
