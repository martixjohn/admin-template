package com.example.demo.api.signup.controller;

import com.example.demo.api.signup.request.LoginRequest;
import com.example.demo.api.signup.request.RegisterRequest;
import com.example.demo.common.annotation.AnonymousAuthentication;
import com.example.demo.common.auth.Authorities;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录和注册控制器
 *
 * @author martix
 * @description
 * @time 2025/4/22
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "用户登录注册")
public class SignUpController {

    private final UserService userService;

    /**
     * 登录
     *
     * @param loginRequest 登录请求
     */
    @AnonymousAuthentication// 只允许匿名用户
    @PostMapping("/login")
    @Operation(summary = "登录")
    public ApiResponse<Void> login(@RequestBody LoginRequest loginRequest) {
        // 登录，一旦发生异常则注册失败
        userService.login(loginRequest.username(), loginRequest.password());
        return ApiResponse.success();
    }

//    @PostMapping("/logout")
//    @Operation(summary = "注销")
//    public void logout() {
//        userService.logout();
//        // 使用CustomLogoutSuccessHandler进行处理
//    }

    @PostMapping("/register")
    @Operation(summary = "注册，分配角色为USER")
    public ApiResponse<Void> register(@RequestBody RegisterRequest registerRequest) {
        // 注册，一旦发生异常则注册失败
        userService.register(registerRequest.username(), registerRequest.password(), Authorities.ROLE_USER);
        return ApiResponse.success();
    }
}
