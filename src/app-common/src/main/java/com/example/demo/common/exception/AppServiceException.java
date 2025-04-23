package com.example.demo.exception;

import com.example.demo.common.response.ResponseCode;
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


    public AppServiceException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }

    public AppServiceException(String msg, ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = msg;
    }

}
