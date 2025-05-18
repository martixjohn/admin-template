package com.example.demo.common.exceptionhandler;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理SpringMVC异常
 *
 * @author martix
 * @description
 * @time 2025/4/27 19:50
 */
@RestControllerAdvice
@Order(1)
public class WebMVCExceptionHandler {
    @ExceptionHandler(exception = {HttpMessageConversionException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<Void> handleHttpMessageException(Exception ex) {
        return ApiResponse.failure(ExceptionCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return ApiResponse.failure(ExceptionCode.BAD_REQUEST, "请求参数错误", errors);
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
