package com.example.demo.common.filter;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.pojo.dto.User;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import com.example.demo.common.manager.JwtManager;
import com.example.demo.common.manager.RedisCacheManager;
import com.example.demo.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 登录filter
 *
 * @author martix
 * @description
 * @time 2025/4/28
 */
@Component
@Slf4j
public class AutoLoginFilter extends OncePerRequestFilter {


    private final JwtManager jwtManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final UserService userService;
    private final String TOKEN_PREFIX = "Bearer ";

    public AutoLoginFilter(JwtManager jwtManager, RedisCacheManager redisCacheManager, UserService userService) {
        this.jwtManager = jwtManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 用户凭证
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }

        //#region 校验TOKEN
        if (!token.startsWith(TOKEN_PREFIX)) {
            badToken(response);
            return;
        }
        token = token.substring(TOKEN_PREFIX.length());
        log.info("token:{}", token);

        boolean validateExpired = false;
        String username = jwtManager.verifyAndGetUsername(token, validateExpired);
        log.info("username:{}", username);

        // 无效token
        if (username == null) {
            badToken(response);
            return;
        }
        //#endregion 校验TOKEN END


        try {
            User user = userService.getUserFromSession(username, token);
            // 视为过期
            if (user == null) {
                ResponseUtil.writeJSONWithDefaultEncoding(response, ApiResponse.failure(ExceptionCode.UNAUTHENTICATED, "会话已过期"));
                return;
            }

            UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
            authentication.setDetails(token);
            securityContextHolderStrategy.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("从redis获取出错", e);
            ResponseUtil.writeJSONWithDefaultEncoding(response,
                    ApiResponse.failure(ExceptionCode.INTERNAL_SERVER_ERROR));

            return;
        }
        chain.doFilter(request, response);

    }

    public void badToken(HttpServletResponse response) throws IOException {
        ResponseUtil.writeJSONWithDefaultEncoding(response, ApiResponse.failure(ExceptionCode.UNAUTHENTICATED, "非法TOKEN"));
    }


}
