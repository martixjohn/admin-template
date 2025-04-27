package com.example.demo.api.user.controller;

import com.example.demo.api.user.request.UserLoginRequest;
import com.example.demo.api.user.request.UserRegisterRequest;
import com.example.demo.api.user.response.UserLoginResponse;
import com.example.demo.common.annotation.PermitAllAuthentication;
import com.example.demo.common.auth.Authorities;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSessionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martix
 * @description
 * @time 2025/4/27 19:27
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "登录和注册")
public class SignUpController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final SecurityContextRepository securityContextRepository;
    //     提前暴露securityContextHolderStrategy防止多个ioc容器争用
    //     https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#use-securitycontextholderstrategy
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final LogoutHandler logoutHandler;
    private final CsrfTokenRepository csrfTokenRepository;
    private final HttpSessionEventPublisher httpSessionEventPublisher;


    @PermitAllAuthentication
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request,
                                                HttpServletRequest servletRequest,
                                                HttpServletResponse servletResponse) {
        String username = request.username();
        String password = request.password();
        userService.checkValidUsernameAndPassword(username, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(token);
        // 认证成功
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticate);
        securityContextHolderStrategy.setContext(context);
        // 内部持久化session
        securityContextRepository.saveContext(context, servletRequest, servletResponse);


        // 生成CSRF Token
        CsrfToken csrfToken = csrfTokenRepository.generateToken(servletRequest);
        log.info("生成CSRF-TOKEN: {}", csrfToken.getToken());
        csrfTokenRepository.saveToken(csrfToken, servletRequest, servletResponse);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setCsrfToken(csrfToken.getToken());
        return ApiResponse.success(userLoginResponse);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) {

        logoutHandler.logout(request, response, authentication);
        // 以上代码代替了以下
//        securityContextHolderStrategy.clearContext();
//        securityContextRepository.saveContext(securityContextHolderStrategy.createEmptyContext(), request, response);
        return ApiResponse.success();
    }

    @PermitAllAuthentication
    @Operation(summary = "用户自行注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody UserRegisterRequest request) {
        String username = request.username();
        String password = request.password();
        userService.checkValidUsernameAndPassword(username, password);
        userService.addUser(username, password, Authorities.ROLE_USER);
        return ApiResponse.success();
    }

}
