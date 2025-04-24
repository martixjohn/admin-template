package com.example.demo.common.config.security;

import cn.hutool.extra.spring.SpringUtil;
import com.example.demo.common.annotation.AnonymousAuthentication;
import com.example.demo.common.annotation.PermitAllAuthentication;
import com.example.demo.common.config.AppSecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author martix
 * @description
 * @time 2025/4/21 23:27
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppSecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AppAccessDeniedHandler accessDeniedHandler,
                                                   AppAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        httpSecurity
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(cfg -> {
                    // 配置anonymous和permitAll
                    configureAuthorities(cfg);
                    // 任何URL都需要认证
                    cfg.anyRequest().authenticated();
                })
                .exceptionHandling(cfg -> {
                    cfg.authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler);
                })
        ;

        log.debug("Spring Security配置完成");
        return httpSecurity.build();
    }

//    默认提供了一个ProviderManager，装配一个DaoAuthenticationProvider
//    @Bean
//    public AuthenticationManager authenticationManager() throws Exception {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        // 自动装配，不用配置
//        // provider.setPasswordEncoder(passwordEncoder());
//        // provider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(provider);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 主要使用的加密算法
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        // 以下为兼容的加密算法
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }


    /**
     * 配置anonymous和permitAll
     * 先配置注解扫描，再配置文件扫描
     */
    private void configureAuthorities(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {

        // 通过注解扫描
        configureAuthoritiesFromAnnotations(registry);

        // 通过配置文件
        configureAuthoritiesFromProperties(registry);
    }

    /**
     * 从配置文件的properties进行配置权限
     */
    private void configureAuthoritiesFromProperties(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        List<String> permitAllUri = securityProperties.getAuthorization().getPermitAllUri();
        List<String> anonymousUri = securityProperties.getAuthorization().getAnonymousUri();

        // 临时使用
        record HttpMethodAndUri(HttpMethod method, String realUri) {
        }

        //  将文件中的uri信息包装成请求方法和uri
        Function<String, HttpMethodAndUri> parseHttpMethodAndUri = (String uri) -> {
            String[] split = uri.split("\\s");
            if (split.length > 2) {
                throw new IllegalArgumentException("错误的uri配置：" + uri);
            }
            HttpMethod method = null;
            String realUri = split[0];
            if (split.length == 2) {
                method = switch (split[0]) {
                    case "GET" -> HttpMethod.GET;
                    case "HEAD" -> HttpMethod.HEAD;
                    case "POST" -> HttpMethod.POST;
                    case "PUT" -> HttpMethod.PUT;
                    case "PATCH" -> HttpMethod.PATCH;
                    case "DELETE" -> HttpMethod.DELETE;
                    case "OPTIONS" -> HttpMethod.OPTIONS;
                    case "TRACE" -> HttpMethod.TRACE;
                    default -> throw new IllegalArgumentException("错误的请求方法：" + split[0]);
                };
                realUri = split[1];
            }
            return new HttpMethodAndUri(method, realUri);
        };

        // 允许所有
        for (String uri : permitAllUri) {
            HttpMethodAndUri httpMethodAndUri = parseHttpMethodAndUri.apply(uri);
            registry.requestMatchers(httpMethodAndUri.method(), httpMethodAndUri.realUri()).permitAll();
        }
        // 仅允许匿名
        for (String uri : anonymousUri) {
            HttpMethodAndUri httpMethodAndUri = parseHttpMethodAndUri.apply(uri);
            registry.requestMatchers(httpMethodAndUri.method(), httpMethodAndUri.realUri()).anonymous();
        }
    }

    /**
     * 扫描注解进行配置权限
     */
    private static void configureAuthoritiesFromAnnotations(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        RequestMappingHandlerMapping mapping = SpringUtil.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            log.trace("配置：{}{}", k, v);
            String[] urls = k.getPatternValues().toArray(new String[0]);
            List<HttpMethod> methods = k.getMethodsCondition().getMethods().stream()
                    .map(RequestMethod::asHttpMethod).toList();
            // anonymous
            if (v.hasMethodAnnotation(AnonymousAuthentication.class)) {
                for (HttpMethod method : methods) {
                    registry.requestMatchers(method, urls).anonymous();
                }
                log.debug("配置匿名访问接口: {}", (Object) urls);
            }
            // permitAll
            if (v.hasMethodAnnotation(PermitAllAuthentication.class)) {
                for (HttpMethod method : methods) {
                    registry.requestMatchers(method, urls).permitAll();
                }
                log.debug("配置所有可访问接口: {}", (Object) urls);
            }
        });
    }


}
