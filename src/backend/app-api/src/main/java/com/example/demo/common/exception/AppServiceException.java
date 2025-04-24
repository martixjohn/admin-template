package com.example.demo.common.exception;

import lombok.Getter;

/**
 * 业务异常
 *
 * @author martix
 * @description
 * @time 2025/4/21 22:19
 */
@Getter
public class AppServiceException extends RuntimeException {
    /**
     * 返回的码
     */
    private final int code;

    /**
     * 返回的信息
     */
    private final String msg;


    public AppServiceException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.msg = exceptionCode.getMsg();
    }

    public AppServiceException(ExceptionCode exceptionCode, String msg) {
        this.code = exceptionCode.getCode();
        this.msg = msg != null ? msg : exceptionCode.getMsg();
    }

}
