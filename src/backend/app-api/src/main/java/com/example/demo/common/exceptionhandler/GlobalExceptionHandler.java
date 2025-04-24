package com.example.demo.common.exceptionhandler;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.exception.ExceptionCode;
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
        return ApiResponse.failure(ExceptionCode.INTERNAL_SERVER_ERROR);
    }
}
