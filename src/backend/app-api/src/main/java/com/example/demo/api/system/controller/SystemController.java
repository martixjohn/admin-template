package com.example.demo.api.system.controller;

import com.example.demo.common.annotation.PermitAllAuthentication;
import com.example.demo.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author martix
 * @description
 * @time 2025/4/22 20:29
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @PermitAllAuthentication
    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success();
    }

    @PermitAllAuthentication
    @GetMapping("/test1")
    public ApiResponse<Void> test1() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        requestAttributes.setAttribute("ikun", "2.5", RequestAttributes.SCOPE_REQUEST);

        return ApiResponse.success();
    }

    @PermitAllAuthentication
    @GetMapping("/test2")
    public ApiResponse<Object> test2() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Object attribute = requestAttributes.getAttribute("ikun", RequestAttributes.SCOPE_REQUEST);

        return ApiResponse.success(attribute);
    }


}
