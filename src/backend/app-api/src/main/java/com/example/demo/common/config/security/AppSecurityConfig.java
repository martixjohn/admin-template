package com.example.demo.common.config.security;

import cn.hutool.extra.spring.SpringUtil;
import com.example.demo.common.annotation.AnonymousAuthorization;
import com.example.demo.common.annotation.PermitAllAuthorization;
import com.example.demo.common.config.security.login.LoginFilter;
import com.example.demo.common.config.security.logout.CustomLogoutSuccessHandler;
import com.example.demo.common.config.security.session.CustomInvalidSessionStrategy;
import com.example.demo.common.config.security.session.CustomSessionInformationExpiredStrategy;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Security配置
 *
 * @author martix
 * @description
 * @time 2025/4/21
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class AppSecurityConfig {

    private final AppSecurityConfigProperties appSecurityConfigProperties;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, LoginFilter loginFilter,
                                            CustomSessionInformationExpiredStrategy customSessionInformationExpiredStrategy,
                                            CustomInvalidSessionStrategy customInvalidSessionStrategy,
                                            CustomLogoutSuccessHandler customLogoutSuccessHandler,
                                            CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {

        // 跨域
        httpSecurity.cors(cfg -> {
            cfg.configurationSource(corsConfigurationSource());
        });
        // CSRF攻击防护
        httpSecurity.csrf(this::configureCsrfProtection);
        // 登出配置
//        httpSecurity.logout(this::configureLogout);
        httpSecurity.logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
            logout.addLogoutHandler(logoutHandler());
            logout.logoutSuccessHandler(customLogoutSuccessHandler);
        });
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        // 授权配置
        httpSecurity.authorizeHttpRequests(this::configureAuthorities);
        // securityContext存储配置，便于全局共享
        httpSecurity.securityContext(cfg -> {
            cfg.securityContextRepository(securityContextRepository())
                    // SecurityContextHolder
                    .requireExplicitSave(true);
        });
        // 异常处理
        httpSecurity.exceptionHandling(except -> {
            except.authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler);
        });
        // 会话管理
        httpSecurity.sessionManagement(cfg -> {
            // 并发会话管理
            cfg.sessionConcurrency(concurrency -> {
                        concurrency.sessionRegistry(concurrentSessionRegistry())
                                .maximumSessions(appSecurityConfigProperties.getSession().getMaximumSessions())
                                .maxSessionsPreventsLogin(false)
                                .expiredSessionStrategy(customSessionInformationExpiredStrategy);
                    })
                    .invalidSessionStrategy(customInvalidSessionStrategy)
                    // session固定攻击
                    .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
        });
        // 自定义登录Filter
        httpSecurity.addFilterAfter(loginFilter, LogoutFilter.class);

        // 在此.build()以获取共享object便于配置
        DefaultSecurityFilterChain build = httpSecurity.build();

        // 填充loginFilter属性
        populateLoginFilter(httpSecurity, loginFilter);


        log.debug("Spring Security配置完成");
        return build;
    }

    @Bean
    FilterRegistrationBean<LoginFilter> loginFilterFilterRegistrationBean(LoginFilter loginFilter) {
        FilterRegistrationBean<LoginFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginFilter);
        filterRegistrationBean.setName("loginFilter");
        // 防止filter 2次调用
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }


//    // 配置异处理
//    private void configureExceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> cfg) {
//        cfg.authenticationEntryPoint(customAuthenticationEntryPoint)
//                .accessDeniedHandler(customAccessDeniedHandler)
//        ;
//    }

    // 配置会话管理
//    private void configureSessionManagement(SessionManagementConfigurer<HttpSecurity> cfg) {
//        // 并发会话管理
//        cfg.sessionConcurrency(concurrency -> {
//                    concurrency.sessionRegistry(concurrentSessionRegistry())
//                            .maximumSessions(appSecurityProperties.getSession().getMaximumSessions())
//                            .maxSessionsPreventsLogin(false)
//                            .expiredSessionStrategy(customSessionInformationExpiredStrategy);
//                })
//                .invalidSessionStrategy(customInvalidSessionStrategy)
//                // session固定攻击
//                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
//    }

    // 配置登出
//    private void configureLogout(LogoutConfigurer<HttpSecurity> cfg) {
//        cfg.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
//        cfg.addLogoutHandler(logoutHandler());
//        cfg.logoutSuccessHandler(customLogoutSuccessHandler);
//    }

    // 配置CSRF保护
    private void configureCsrfProtection(CsrfConfigurer<HttpSecurity> cfg) {
        // 显式设置HttpSessionCsrfTokenRepository
        cfg.csrfTokenRepository(csrfTokenRepository());
        cfg.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
        // 所有请求都需要携带CSRF Token
        cfg.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/**"))
                .ignoringRequestMatchers(new AntPathRequestMatcher("/login", "POST"));
        // 完全放开
        appSecurityConfigProperties.getFullyOpenUris()
                .forEach(e -> {
                    log.info("完全放开: {}", e);
                    AntPathRequestMatcher requestMatcher = parsePropertiesUri(e);
                    cfg.ignoringRequestMatchers(requestMatcher);
                });
    }

    // 填充LoginFilter属性
    private static void populateLoginFilter(HttpSecurity httpSecurity, LoginFilter loginFilter) {
        SessionAuthenticationStrategy sessionAuthenticationStrategy = httpSecurity.getSharedObject(SessionAuthenticationStrategy.class);
        assert sessionAuthenticationStrategy != null;
        loginFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);

        MessageSource messageSource = httpSecurity.getSharedObject(MessageSource.class);
        assert messageSource != null;
        loginFilter.setMessageSource(messageSource);

        ApplicationEventPublisher eventPublisher = httpSecurity.getSharedObject(ApplicationEventPublisher.class);
        assert eventPublisher != null;
        loginFilter.setApplicationEventPublisher(eventPublisher);

        @SuppressWarnings("unchecked")
        AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = (AuthenticationDetailsSource<HttpServletRequest, ?>) httpSecurity.getSharedObject(AuthenticationDetailsSource.class);
        if (authenticationDetailsSource != null) {
            loginFilter.setAuthenticationDetailsSource(authenticationDetailsSource);
        }

        RememberMeServices rememberMeServices = httpSecurity.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            loginFilter.setRememberMeServices(rememberMeServices);
        }
    }


    // 跨域配置
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // CSRF攻击防护Token存储
    @Bean
    CsrfTokenRepository csrfTokenRepository() {
        // 显式设置
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }

    // 登出处理
    @Bean
    LogoutHandler logoutHandler() {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.setSecurityContextRepository(securityContextRepository());
        HeaderWriterLogoutHandler headerWriterLogoutHandler = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.CACHE, ClearSiteDataHeaderWriter.Directive.COOKIES));
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler("JSESSIONID");
        return new CompositeLogoutHandler(securityContextLogoutHandler, headerWriterLogoutHandler, cookieClearingLogoutHandler);
    }

    // 密码加密
    @Bean
    PasswordEncoder passwordEncoder() {
        // 主要使用的加密算法
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        // 以下为兼容的加密算法
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    // 认证配置
    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        // 用于处理UserDetails, UsernamePasswordAuthenticationToken
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // 自动装配，非显式设置
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // UsernameNotFoundExceptions -> BadCredentialsException
        daoAuthenticationProvider.setHideUserNotFoundExceptions(true);
        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);
        // 认证后自动清除密码
        providerManager.setEraseCredentialsAfterAuthentication(true);
        return providerManager;
    }


    // 临时存储/持久化SecurityContext
    @Bean
    SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
    }

//    // 角色层次结构
//    @Bean
//    RoleHierarchy roleHierarchy() {
//        return RoleHierarchyImpl.withRolePrefix("ROLE_")
//                .role(Authorities.ROLE_SUPER_ADMIN).implies(Authorities.ROLE_ADMIN)
//                .role(Authorities.ROLE_ADMIN).implies(Authorities.ROLE_USER)
//                .build();
//    }

    // and, if using pre-post method security also add
//    @Bean
//    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(/*RoleHierarchy roleHierarchy*/) {
//        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy);
//        return expressionHandler;
//    }

    // 为了识别Session生命周期
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    // 并发控制session存储
    @Bean
    SessionRegistry concurrentSessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 配置权限
     * 先配置注解扫描，再配置文件扫描
     */
    private void configureAuthorities(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {

        // 通过注解扫描
        configureAuthoritiesFromAnnotations(registry);

        // 通过配置文件
        configureAuthoritiesFromProperties(registry);

        registry.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll();
        // 任何URL都需要认证
        registry.anyRequest().authenticated();
    }

    // 从配置文件的properties进行配置权限
    private void configureAuthoritiesFromProperties(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {

        // fully-open
        appSecurityConfigProperties.getFullyOpenUris()
                .forEach(e -> {
                    registry.requestMatchers(parsePropertiesUri(e)).permitAll();
                });
        // permit-all
        appSecurityConfigProperties.getAuthorization().getPermitAllUri()
                .forEach(e -> {
                    registry.requestMatchers(parsePropertiesUri(e)).permitAll();
                });

        // anonymous
        appSecurityConfigProperties.getAuthorization().getAnonymousUri()
                .forEach(e -> {
                    registry.requestMatchers(parsePropertiesUri(e)).permitAll();
                });
    }

    // 扫描注解进行配置权限
    private void configureAuthoritiesFromAnnotations(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        RequestMappingHandlerMapping mapping = SpringUtil.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        handlerMethods.forEach((info, hm) -> {
            log.trace("配置：{}{}", info, hm);
            String[] urls = info.getPatternValues().toArray(new String[0]);
            List<HttpMethod> methods = info.getMethodsCondition().getMethods().stream().map(RequestMethod::asHttpMethod).toList();
            // anonymous
            if (hm.hasMethodAnnotation(AnonymousAuthorization.class) || hm.getBeanType().isAnnotationPresent(AnonymousAuthorization.class)) {
                for (HttpMethod method : methods) {
                    registry.requestMatchers(method, urls).anonymous();
                }
                log.debug("配置匿名访问接口: {}", (Object) urls);
            }
            // permitAll
            if (hm.hasMethodAnnotation(PermitAllAuthorization.class) || hm.getBeanType().isAnnotationPresent(PermitAllAuthorization.class)) {
                for (HttpMethod method : methods) {
                    registry.requestMatchers(method, urls).permitAll();
                }
                log.debug("配置所有可访问接口: {}", (Object) urls);
            }
        });
    }


    //  将文件中的uri信息包装成请求方法和uri
    private static AntPathRequestMatcher parsePropertiesUri(String uri) {
        String[] split = uri.split("\\s");
        if (split.length > 2) {
            throw new IllegalArgumentException("错误的uri配置：" + uri);
        }

        if (split.length == 2) {
            String method = split[0];
            String realUri = split[1];
            switch (method) {
                case "GET":
                case "HEAD":
                case "POST":
                case "PUT":
                case "PATCH":
                case "DELETE":
                case "OPTIONS":
                case "TRACE":
                    break;
                default:
                    throw new IllegalArgumentException("错误的请求方法：" + split[0]);
            }
            return new AntPathRequestMatcher(realUri, method);
        } else {
            return new AntPathRequestMatcher(uri);
        }

    }

}
