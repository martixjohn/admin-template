package com.example.demo.api.user.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.api.user.request.UserLoginRequest;
import com.example.demo.api.user.request.UserRegisterRequest;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martix
 * @description
 * @time 2025/4/22 0:41
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    /**
     * 登录
     *
     * @param loginRequest 登录请求
     */
    @PostMapping("/login")
    public ApiResponse<Void> login(UserLoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        // 登录，一旦发生异常则注册失败
        userService.login(username, password);
        return ApiResponse.success();
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(UserRegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        // 注册，一旦发生异常则注册失败
        userService.register(username, password);
        return ApiResponse.success();
    }
}
