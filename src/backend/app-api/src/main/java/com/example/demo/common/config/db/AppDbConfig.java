package com.example.demo.common.config.db;

import com.example.demo.common.auth.Authorities;
import com.example.demo.common.pojo.entity.RolePO;
import com.example.demo.common.pojo.entity.UserPO;
import com.example.demo.common.pojo.entity.UserRolePO;
import com.example.demo.repository.user.RoleMapper;
import com.example.demo.repository.user.UserMapper;
import com.example.demo.repository.user.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库配置
 *
 * @author martix
 * @description
 * @time 2025/4/26 16:25
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppDbConfig {

    private final AppDbProperties appDbProperties;

    private final UserMapper userMapper;

    private final UserRoleMapper userRoleMapper;

    private final RoleMapper roleMapper;

    private final PasswordEncoder passwordEncoder;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 需要初始化数据库
        if (appDbProperties.isEnableInit()) {
            /* 添加超级管理员 */
            UserPO userPO = new UserPO();
            userPO.setUsername(appDbProperties.getSuperAdminUsername());
            userPO.setPassword(passwordEncoder.encode(appDbProperties.getSuperAdminPassword()));
            userMapper.insert(userPO);

            RolePO rolePO = new RolePO();
            rolePO.setName(Authorities.ROLE_SUPER_ADMIN);
            roleMapper.insert(rolePO);

            UserRolePO userRolePO = new UserRolePO();
            userRolePO.setUserId(userPO.getId());
            userRolePO.setRoleId(rolePO.getId());
            userRoleMapper.insert(userRolePO);
            log.info("初始化超级管理员成功！username={}  password={}", appDbProperties.getSuperAdminUsername(), appDbProperties.getSuperAdminPassword());
        }
    }
}
