package com.example.demo.common.config.security;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 访问无权限处理
 *
 * @author martix
 * @description
 * @time 2025/4/24
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.debug("Access denied: {}", accessDeniedException.getMessage());
        ResponseUtil.writeJSONWithDefaultEncoding(response,
                ApiResponse.failure(ExceptionCode.FORBIDDEN, "无权限访问"));
    }
}
