package com.example.demo.common.config.security;

import com.example.demo.user.pojo.entity.User;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author martix
 * @description DaoAuthenticationProvider使用
 * @time 2025/4/22 0:59
 */
@Component
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userService.getByUsername(username);
        // spring自带的user
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(byUsername.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
