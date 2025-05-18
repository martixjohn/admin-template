package com.example.demo.api.user.controller;

import com.example.demo.api.user.request.UserLoginRequest;
import com.example.demo.api.user.response.UserLoginVO;
import com.example.demo.common.annotation.AnonymousAuthorization;
import com.example.demo.common.manager.JwtManager;
import com.example.demo.common.pojo.dto.User;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 登录认证
 *
 * @author martix
 * @time 2025/5/1
 */
@Slf4j
@RestController
@Tag(name = "login", description = "登录和登出")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtManager jwtManager;

    @Operation(summary = "登录")
    @AnonymousAuthorization
    @PostMapping("/login")
    @Transactional
    public ApiResponse<UserLoginVO> login(@RequestBody UserLoginRequest login) {
        log.info("用户尝试登录：{}", login);
        String username = login.username();
        String password = login.password();
        userService.checkValidUsernameAndPassword(username, password);
        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // may throw AuthenticationException
        Authentication authenticated = authenticationManager.authenticate(unauthenticated);
        // 认证成功
        User user = (User) authenticated.getPrincipal();
        log.info("用户登录认证成功: {}", user);

        // 生成JWT
        String token = jwtManager.generateToken(user);
        UserLoginVO loginVO = UserLoginVO.of(user, token);

        userService.addUserToSession(user, token);

        log.info("用户登录成功: {}", loginVO);
        return ApiResponse.success(loginVO);
    }

    @Operation(summary = "登出")
    @GetMapping("/logout")
    @Transactional
    public ApiResponse<Void> logout(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        userService.invalidateOneSessionOfUser(username, (String) authentication.getDetails());
        log.info("用户{}登出成功", username);
        return ApiResponse.success();
    }

}
