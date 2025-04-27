package com.example.demo.common.exceptionhandler;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author martix
 * @description
 * @time 2025/4/27 19:54
 */
@RestControllerAdvice
@Slf4j
public class SecurityExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<Void> handleFallback(BadCredentialsException ex) {
        log.info("用户输入错误账号或密码: {}", ex.getMessage());
        return ApiResponse.failure(ExceptionCode.BAD_REQUEST, ex.getMessage());
    }
}
