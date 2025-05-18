package com.example.demo.common.request;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.pojo.dto.QueryCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 批量查询请求
 *
 * @author martix
 * @description
 * @time 5/12/25 5:16 PM
 */
@Valid
public record PageQueryRequest(
        @Schema(description = "页大小", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1)
        @NotNull
        Long pageSize,

        @Schema(description = "当前页数, 1开始", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(1)
        @NotNull
        Long current,

        @Schema(description = "排序字段，如[\"username\", \"asc\"]", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @Size(min = 1)
        // 参数校验在调用toPage发生
        List<List<String>> orders,

        // 参数校验在调用toQueryCondition发生
        // 注：最终定位到数据库字段
        @Schema(description = "查询条件，二元运算，逻辑AND拼接. 如 [[\"username\",\"like\",\"ali\"],[\"age\",\">=\",\"20\"]]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        List<List<String>> queryCondition
) {
    /**
     * 获得查询条件
     *
     * @param allowedField   只允许的字段
     * @param fieldConverter 字段转换
     * @return 可能为null
     * @throws AppServiceException 查询条件非法
     * @description 调用会进行检验
     */
    @Nullable
    public List<QueryCondition> toQueryConditions(@Nonnull Set<String> allowedField, @Nonnull Function<String, String> fieldConverter) throws AppServiceException {
        if (queryCondition == null || queryCondition.isEmpty()) {
            return null;
        }
        List<QueryCondition> conditions = new ArrayList<>(queryCondition.size());

        Set<String> allows = allowedField.stream().map(fieldConverter).collect(Collectors.toSet());
        for (List<String> strings : queryCondition) {
            if (strings == null || strings.size() != 3) {
                throw new AppServiceException(ExceptionCode.BAD_REQUEST, "查询条件非法");
            }
            String field = strings.get(0);
            if (StrUtil.isBlank(field)) {
                throw new AppServiceException(ExceptionCode.BAD_REQUEST, "查询条件非法");
            }
            field = fieldConverter.apply(field);
            if (!allows.contains(field)) {
                throw new AppServiceException(ExceptionCode.BAD_REQUEST, "查询条件非法: 不允许字段" + field);
            }
            // 内部检查了第二个参数
            QueryCondition condition = new QueryCondition(field, strings.get(1), strings.get(2));
            conditions.add(condition);
        }
        return conditions;
    }

    public List<QueryCondition> toQueryConditions(@Nonnull Set<String> allowedField) {
        return toQueryConditions(allowedField, QueryCondition.defaultFieldConverter);
    }

    /**
     * 转化为mybatis plus标准的page，
     *
     * @param fieldConverter 字段转换
     * @description 调用会自动进行参数校验
     */
    @Nonnull
    public <T> IPage<T> toPage(@Nonnull Function<String, String> fieldConverter) {

        var orderItems = new ArrayList<OrderItem>(orders.size());
        for (List<String> order : orders) {
            if (order == null || order.size() != 2) {
                throw new AppServiceException(ExceptionCode.BAD_REQUEST, "排序字段非法");
            }
            String field = fieldConverter.apply(order.get(0));
            orderItems.add("asc".equalsIgnoreCase(order.get(1)) ? OrderItem.asc(field) : OrderItem.desc(field));
        }

        Page<T> page = new Page<>(current, pageSize);
        page.setOrders(orderItems);
        return page;
    }

    public <T> IPage<T> toPage() {
        return toPage(QueryCondition.defaultFieldConverter);
    }
}
