package com.example.demo.common.config.security;

import com.example.demo.common.pojo.service.User;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity UserDetailsService
 *
 * @author martix
 * @description DaoAuthenticationProvider使用
 * @time 2025/4/22 0:59
 */
@Component
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    // 注意永远返回非空
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getFullInfoByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 找不到"));

        // 权限处理
        List<SimpleGrantedAuthority> permissions = user.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(permissions);
        authorities.addAll(roles);

        // spring自带的user
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
