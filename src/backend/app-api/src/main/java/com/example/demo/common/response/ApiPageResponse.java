package com.example.demo.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.exception.ExceptionCode;

import java.util.List;

/**
 * 分页返回
 *
 * @author martix
 * @description
 * @time 5/12/25 9:51 AM
 */
public class ApiPageResponse<T> extends ApiResponse<PageVO<T>> {
    private ApiPageResponse(int code, String msg, PageVO<T> data) {
        super(code, msg, data);
    }

    public static <R> ApiPageResponse<R> success(long total, long current, long pageSize, List<R> records) {
        ExceptionCode success = ExceptionCode.SUCCESS;
        return new ApiPageResponse<R>(success.getCode(), success.getMsg(),
                new PageVO<R>(total, current, pageSize, records));
    }

    public static <R> ApiPageResponse<R> success(IPage<R> page) {
        ExceptionCode success = ExceptionCode.SUCCESS;
        return new ApiPageResponse<R>(success.getCode(), success.getMsg(),
                new PageVO<R>(page.getTotal(),page.getCurrent(), page.getSize(), page.getRecords()));
    }
}
