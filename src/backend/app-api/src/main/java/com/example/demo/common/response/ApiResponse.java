package com.example.demo.common.response;

import com.example.demo.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回响应体
 * @param <T> data字段类型
 * @author martix
 * @description 主要以JSON形式序列化
 * @time 2025/4/21 22:12
 */
@Getter
@Setter
public class ApiResponse<T> {
    private final int code;
    private final String msg;
    private final T data;

    private ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> ApiResponse<T> success(T data) {
        ExceptionCode success = ExceptionCode.SUCCESS;
        return new ApiResponse<>(success.getCode(), success.getMsg(), data);
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> failure(ExceptionCode exceptionCode) {
        return new ApiResponse<>(exceptionCode.getCode(), exceptionCode.getMsg(), null);
    }

    public static ApiResponse<Void> failure(ExceptionCode exceptionCode, String msg) {
        return new ApiResponse<>(exceptionCode.getCode(), msg, null);
    }

    public static <T> ApiResponse<T> failure(ExceptionCode exceptionCode, String msg, T data) {
        return new ApiResponse<>(exceptionCode.getCode(), msg, data);
    }

}
