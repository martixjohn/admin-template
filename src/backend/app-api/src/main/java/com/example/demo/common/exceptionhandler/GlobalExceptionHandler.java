package com.example.demo.common.exceptionhandler;

import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:10
 */
@Slf4j
@RestControllerAdvice
@Order(100)
public class GlobalExceptionHandler {

    // 全局保底异常
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleFallback(Exception ex) {
        log.warn("全局异常: ", ex);
        return ApiResponse.failure(ExceptionCode.INTERNAL_SERVER_ERROR);
    }

    // 处理业务异常
    @ExceptionHandler(AppServiceException.class)
    public ApiResponse<Void> handleAppServiceException(AppServiceException ex) {
        int code = ex.getCode();
        String msg = ex.getMsg();
        log.info("返回异常: code={} msg={}", code, msg);
        return ApiResponse.failure(code, msg);
    }
}
