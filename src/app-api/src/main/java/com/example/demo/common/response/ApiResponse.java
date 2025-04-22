package com.example.demo.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 返回响应
 * @author martix
 * @description
 * @time 2025/4/21 22:12
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse<T> {
    private final T data;
    private final int code;
    private final String msg;

    public ApiResponse(T data, ResponseCode responseCode) {
        this.data = data;
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public ApiResponse(T data, ResponseCode responseCode, String msg) {
        this.data = data;
        this.code = responseCode.getCode();
        this.msg = msg;
    }


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, ResponseCode.SUCCESS);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(null, ResponseCode.SUCCESS);
    }
}
