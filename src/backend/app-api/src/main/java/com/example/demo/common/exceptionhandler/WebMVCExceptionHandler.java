package com.example.demo.common.exceptionhandler;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 处理SpringMVC异常
 * @author martix
 * @description
 * @time 2025/4/27 19:50
 */
@RestControllerAdvice
public class WebMVCExceptionHandler {
    @ExceptionHandler(exception = HttpMessageConversionException.class)
    public ApiResponse<Void> handleHttpMessageException(Exception ex) {
        return ApiResponse.failure(ExceptionCode.BAD_REQUEST);
    }

    @ExceptionHandler(exception = NoResourceFoundException.class)
    public ApiResponse<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ApiResponse.failure(ExceptionCode.NOT_FOUND);
    }

    @ExceptionHandler(exception = HttpMediaTypeException.class)
    public ApiResponse<Void> handleHttpMediaTypeException(HttpMediaTypeException ex) {
        return ApiResponse.failure(ExceptionCode.BAD_REQUEST);
    }
}
