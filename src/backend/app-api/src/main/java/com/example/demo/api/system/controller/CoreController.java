package com.example.demo.api.system.controller;

import com.example.demo.common.annotation.PermitAllAuthentication;
import com.example.demo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author martix
 * @description
 * @time 2025/4/22 20:29
 */
@RestController
@RequestMapping("/system/core")
@Tag(name = "系统")
public class CoreController {

    @PermitAllAuthentication
    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success();
    }

    @PermitAllAuthentication
    @GetMapping("/test/set-attr")
    public ApiResponse<Void> test1() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        requestAttributes.setAttribute("ikun", "2.5", RequestAttributes.SCOPE_SESSION);

        return ApiResponse.success();
    }

    @PermitAllAuthentication
    @GetMapping("/test/get-attr")
    public ApiResponse<Object> test2() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Object attribute = requestAttributes.getAttribute("ikun", RequestAttributes.SCOPE_SESSION);

        return ApiResponse.success(attribute);
    }

    @PermitAllAuthentication
    @GetMapping("/test/info")
    public ApiResponse<Authentication> info(Authentication authentication) {
        return ApiResponse.success(authentication);
    }


}
