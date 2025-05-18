package com.example.demo.common.exceptionhandler;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author martix
 * @description
 * @time 2025/4/27 19:54
 */
@RestControllerAdvice
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    // 所有认证失败
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Void> handleAllAuthenticationException(AuthenticationException ex) {
//        logoutHandler.logout(request, response, authentication);
        return ApiResponse.failure(ExceptionCode.UNAUTHENTICATED, ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ApiResponse<Void> handleAllAuthenticationException(AuthorizationDeniedException ex) {
        return ApiResponse.failure(ExceptionCode.FORBIDDEN, "不允许访问");
    }

//    // 密码错误
//    @ExceptionHandler(BadCredentialsException.class)
//    public ApiResponse<Void> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        log.info("用户输入错误账号或密码: {}", ex.getMessage());
////        logoutHandler.logout(request, response, authentication);
//        return ApiResponse.failure(ExceptionCode.BAD_REQUEST, ex.getMessage());
//    }
//
//    // 账户状态异常
//    @ExceptionHandler(AccountStatusException.class)
//    public ApiResponse<Void> handleAccountStatusException(AccountStatusException ex, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        log.info("账户状态异常：{}", ex.getMessage());
////        logoutHandler.logout(request, response, authentication);
//        return ApiResponse.failure(ExceptionCode.FORBIDDEN, ex.getMessage());
//    }
}
