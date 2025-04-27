package com.example.demo.common.config.security;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;

/**
 * @author martix
 * @description
 * @time 2025/4/27 15:18
 */
@Component
@Slf4j
@Deprecated
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功 {}", authentication);
        ResponseUtil.writeJSONWithDefaultEncoding(response,
                ApiResponse.success(null, "登录成功"));
    }
}
