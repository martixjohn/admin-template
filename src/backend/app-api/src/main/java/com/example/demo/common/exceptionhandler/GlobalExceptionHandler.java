package com.example.demo.common.exceptionhandler;

import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常 fallback
 * @author martix
 * @description
 * @time 2025/5/12
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
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
        ExceptionCode code = ex.getCode();
        String msg = ex.getMsg();
        log.info("返回异常: code={} msg={}", code, msg);
        return ApiResponse.failure(code, msg);
    }
}
