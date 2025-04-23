package com.example.demo.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 返回响应中code，msg封装
 *
 * @author martix
 * @description code采用HTTP状态码与内部状态码结合的方式，除了正常以外
 * @time 2025-4-23
 */
@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(0, "正常"),
    BAD_REQUEST(400_000, "错误请求"),
    NOT_FOUND(404_000, "找不到资源"),
    INTERNAL_SERVER_ERROR(500_000, "内部异常"),
    ;
    private final int code;
    private final String msg;

}
