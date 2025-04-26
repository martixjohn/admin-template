package com.example.demo.common.config.security;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;

/**
 * 访问无权限处理
 *
 * @author martix
 * @description
 * @time 2025/4/24 13:26
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    private final String encoding;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper, CharacterEncodingFilter encodingFilter) {
        this.objectMapper = objectMapper;
        this.encoding = encodingFilter.getEncoding();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.debug("Access denied: {}", accessDeniedException.getMessage());
        // 注意：此处不应抛出异常
        String json = objectMapper.writeValueAsString(ApiResponse.failure(ExceptionCode.FORBIDDEN));
        log.debug("AccessDeniedHandler json={}", json);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(encoding);
        response.getWriter().write(json);
        response.getWriter().close();
    }
}
