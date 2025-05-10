package com.example.demo.common.exception;

import lombok.Getter;

/**
 * 错误码
 * 对应返回响应中code，msg封装
 *
 * @author martix
 * @description code采用HTTP状态码与内部状态码结合的方式，除了正常以外
 * @time 2025-4-23
 */
@Getter
public enum ExceptionCode {
    SUCCESS(0, "正常"),
    BAD_REQUEST(400_000, "错误请求"),
    // 完全未经认证
    UNAUTHENTICATED(401_000, "未认证"),
    // 认证通过，无权限
    FORBIDDEN(403_000, "访问被拒绝"),
    NOT_FOUND(404_000, "找不到资源"),
    INTERNAL_SERVER_ERROR(500_000, "内部异常"),
    ;
    private final int code;
    private final String msg;

    ExceptionCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
