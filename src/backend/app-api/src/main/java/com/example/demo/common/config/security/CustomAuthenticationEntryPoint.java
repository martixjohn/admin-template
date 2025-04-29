package com.example.demo.common.config.security;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;

/**
 * 认证失败处理
 *
 * @author martix
 * @description
 * @time 2025/4/24
 */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("AuthenticationEntryPoint: url={}  exp={}", request.getRequestURI(), authException.getMessage());
        log.trace("AuthenticationEntryPoint exception", authException);

        ResponseUtil.writeJSONWithDefaultEncoding(response,
                ApiResponse.failure(ExceptionCode.UNAUTHENTICATED));
    }
}
