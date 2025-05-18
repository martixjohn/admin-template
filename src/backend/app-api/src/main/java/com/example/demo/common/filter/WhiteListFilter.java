package com.example.demo.common.filter;

import com.example.demo.common.config.security.AppSecurityConfig;
import com.example.demo.common.config.security.AppSecurityConfigProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.util.*;

/**
 * 处理FullyOpenUris，直接放行，不会有任何校验
 *
 * @author martix
 * @time 5/11/25 1:16 PM
 */
@Slf4j
@Component
public class WhiteListFilter extends GenericFilterBean {
    private final DispatcherServlet dispatcherServlet;
    private final List<AntPathRequestMatcher> skipMatchers;

    public WhiteListFilter(DispatcherServlet dispatcherServlet, AppSecurityConfigProperties appSecurityConfigProperties) {
        this.dispatcherServlet = dispatcherServlet;
        this.skipMatchers = appSecurityConfigProperties.getWhiteListUri().stream().map(AppSecurityConfig::parsePropertiesUri).toList();
    }


    /**
     * 是否跳过SecurityFilter
     */
    private boolean isSkipSecurityFilters(HttpServletRequest request) {
        for (AntPathRequestMatcher skipMatcher : skipMatchers) {
            if (skipMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            if (isSkipSecurityFilters(httpRequest)) {
                log.info("完全放行: {}", httpRequest.getRequestURI());
                dispatcherServlet.service(httpRequest, response);
            } else {
                chain.doFilter(httpRequest, response);
            }
        }

    }
}
