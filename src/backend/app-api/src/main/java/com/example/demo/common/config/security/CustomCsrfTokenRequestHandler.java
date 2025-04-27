package com.example.demo.common.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @description
 * @author martix
 * @time 2025/4/27 23:07
 */
@Slf4j
@Component
public class CustomCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        log.info("CSRF token from tokenRepository: {}", csrfToken.get().getToken());
    }
}
