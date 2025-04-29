package com.example.demo.common.config.security.login;

import com.example.demo.common.exception.AppServiceException;
import com.example.demo.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
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
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    private final UserService userService;

    public LoginFilter(ObjectMapper objectMapper, UserService userService) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        String contentType = request.getContentType();
        if (!MediaType.valueOf(contentType).equals(MediaType.APPLICATION_JSON)) {
            throw new AuthenticationServiceException("Unsupported content type: " + contentType);
        }
        // 反序列化请求体
        BufferedReader reader = request.getReader();
        UserLoginRequest userLoginRequest = null;
        try {
            userLoginRequest = objectMapper.readValue(reader, UserLoginRequest.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
        // 校验用户名密码的合法性
        String username = userLoginRequest.username();
        String password = userLoginRequest.password();
        try {
            userService.checkValidUsernameAndPassword(username, password);
        } catch (AppServiceException ex) {
            throw new AuthenticationServiceException(ex.getMessage());
        }
        // 认证
        Authentication authenticate = getAuthenticationManager()
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password));
        log.info("Authenticated user: {}", authenticate.getName());

        return authenticate;
    }

    // 自动注入
    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    // 自动注入
    @Autowired
    @Override
    public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
        super.setSecurityContextRepository(securityContextRepository);
    }

    // 自动注入
    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    // 自动注入
    @Autowired
    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }


    @Override
    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {
        super.setSessionAuthenticationStrategy(sessionStrategy);
    }


    @Override
    public void setMessageSource(MessageSource messageSource) {
        super.setMessageSource(messageSource);
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        super.setApplicationEventPublisher(eventPublisher);
    }

    @Override
    public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        super.setAuthenticationDetailsSource(authenticationDetailsSource);
    }


    @Override
    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        super.setRememberMeServices(rememberMeServices);
    }

}
