package com.example.demo.common.config.security;

import com.example.demo.common.config.app.AppSecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author martix
 * @description
 * @time 2025/4/21 23:27
 */
@Configuration
// Spring Security注解
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final AppSecurityProperties properties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {

//                    // 从配置文件中读取
//                    parseAuth(properties.getPermitAllUri(),
//                            (httpMethod, uri) ->
//                                    auth.requestMatchers(httpMethod, uri).permitAll()
//                    );
//                    parseAuth(properties.getAnonymousUri(),
//                            (httpMethod, uri) ->
//                                    auth.requestMatchers(httpMethod, uri).anonymous()
//                    );

                    // 这里不要写url，应当使用注解

                    // 任何URL都需要认证
                    auth.anyRequest().authenticated();
                });

        return httpSecurity.build();
    }


//    private void parseAuth(List<String> auths, BiConsumer<HttpMethod, String> fn) {
//        for (String uri : auths) {
//            // 空格分割，方法和uri匹配
//            String[] split = uri.split("\\s");
//            if (split.length > 2) {
//                throw new RuntimeException("错误的属性：app.security.anonymous-uri" + uri);
//            }
//            HttpMethod method = null;
//            String realUri = split[0];
//            if (split.length == 2) {
//                method = HttpMethod.valueOf(split[0]);
//                realUri = split[1];
//            }
//            fn.accept(method, realUri);
//        }
//    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationProvider provider = new DaoAuthenticationProvider();
        return new ProviderManager(provider);
    }

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

}
