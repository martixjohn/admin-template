package com.example.demo.common.config.security.login;

import com.example.demo.common.pojo.service.User;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 登录成功处理
 *
 * @author martix
 * @description
 * @time 2025/4/28 19:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final CsrfTokenRepository csrfTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);
        User principal = (User) authentication.getPrincipal();
        log.info("为{}生成CsrfToken: {}", principal.getUsername(), csrfToken.getToken());
        principal.setLastLoginTime(LocalDateTime.now());

        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setCsrfToken(csrfToken.getToken());
        ResponseUtil.writeJSONWithDefaultEncoding(response, ApiResponse.success(userLoginVO, "登录成功"));
    }
}
