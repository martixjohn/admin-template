package com.example.demo.common.exceptionhandler;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author martix
 * @description
 * @time 2025/4/21 23:42
 */
@Slf4j
@ControllerAdvice
public class SecurityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Void> handleAuthenticationException(AuthenticationException exception) {
        log.info("认证失败: {}", exception.getMessage());
        log.debug("AuthenticationException", exception);
        return ApiResponse.failure(ExceptionCode.FORBIDDEN);
    }
}
