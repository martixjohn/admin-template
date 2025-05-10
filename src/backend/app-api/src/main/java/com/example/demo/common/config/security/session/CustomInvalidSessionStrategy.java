package com.example.demo.common.config.security.session;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 会话无效策略
 * @author martix
 * @description
 * @time 2025/4/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    @Resource
    private LogoutHandler logoutHandler;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        log.info("检测到无效session：authentication={}", authentication);
        logoutHandler.logout(request, response, authentication);

        ResponseUtil.writeJSONWithDefaultEncoding(response,
                ApiResponse.failure(ExceptionCode.UNAUTHENTICATED, "会话无效"));

    }

}
