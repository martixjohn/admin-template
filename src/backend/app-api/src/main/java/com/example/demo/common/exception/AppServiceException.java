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
     * 对应前端的code
     */
    private final ExceptionCode code;

    /**
     * 默认ExceptionCode.msg，可以覆盖
     */
    private final String msg;


    public AppServiceException(ExceptionCode exceptionCode) {
        this.code = exceptionCode;
        this.msg = exceptionCode.getMsg();
    }

    public AppServiceException(ExceptionCode exceptionCode, String msg) {
        this.code = exceptionCode;
        this.msg = msg;
    }

}
