package com.example.demo.common.config.security;

import com.example.demo.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;

/**
 * @author martix
 * @description
 * @time 2025/4/26 22:58
 */
@Component
@Deprecated
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;

    private final String encoding;

    public CustomLogoutSuccessHandler(ObjectMapper objectMapper, CharacterEncodingFilter encodingFilter) {
        this.objectMapper = objectMapper;
        this.encoding = encodingFilter.getEncoding();
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登出成功 {}", authentication);
        // 注意：此处不应抛出异常
        String json = objectMapper.writeValueAsString(ApiResponse.success(null, "登出成功"));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(encoding);
        response.getWriter().write(json);
        response.getWriter().close();
    }
}
