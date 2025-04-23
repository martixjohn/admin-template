package com.example.demo.api.common.config.exceptionhandler;

import com.example.demo.api.common.response.ApiResponse;
import com.example.demo.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:10
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 保底异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleAll(Exception ex) {
        log.warn("全局异常: ", ex);
        return new ApiResponse<>(null, ResponseCode.INTERNAL_SERVER_ERROR);
    }
}
