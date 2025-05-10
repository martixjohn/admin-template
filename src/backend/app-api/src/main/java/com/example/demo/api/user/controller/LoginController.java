package com.example.demo.api.user.controller;

import com.example.demo.common.annotation.PermitAllAuthorization;
import com.example.demo.common.config.security.login.UserLoginRequest;
import com.example.demo.common.config.security.login.UserLoginVO;
import com.example.demo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 登录
 *
 * @author martix
 * @description 只用于实际接口文档生成!!!具体逻辑由Spring Security的FilterChain代替
 * @time 2025/5/9 14:57
 */
@RestController
@Tag(name = "login", description = "登录和登出")
public class LoginController {

    // 仅用于接口文档生成
    @Operation(summary = "登录")
    @PermitAllAuthorization
    @PostMapping("/login")
    public ApiResponse<UserLoginVO> login(@RequestBody UserLoginRequest login) {
        return ApiResponse.success(null);
    }

    // 仅用于接口文档生成
    @Operation(summary = "登出")
    @PermitAllAuthorization
    @GetMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success();
    }

}
