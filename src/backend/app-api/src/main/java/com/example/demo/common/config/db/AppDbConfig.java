package com.example.demo.common.config.db;

import com.example.demo.common.auth.Authorities;
import com.example.demo.common.pojo.po.RolePO;
import com.example.demo.common.pojo.po.UserPO;
import com.example.demo.common.pojo.po.UserRolePO;
import com.example.demo.repository.user.RoleMapper;
import com.example.demo.repository.user.UserMapper;
import com.example.demo.repository.user.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

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

    private final AppDbConfigProperties appDbConfigProperties;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final SqlSessionFactory sqlSessionFactory;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onApplicationEvent(ApplicationStartedEvent event) throws IOException {
        // 需要初始化数据库
        if (appDbConfigProperties.isEnableInit()) {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource("classpath:init.sql");
            try(SqlSession sqlSession = sqlSessionFactory.openSession(false)){

                ScriptUtils.executeSqlScript(sqlSession.getConnection(), resource);

                /* 添加超级管理员 */
                UserPO userPO = new UserPO();
                userPO.setUsername(appDbConfigProperties.getSuperAdminUsername());
                userPO.setPassword(passwordEncoder.encode(appDbConfigProperties.getSuperAdminPassword()));
                userMapper.insert(userPO);

                RolePO rolePO = new RolePO();
                rolePO.setName(Authorities.ROLE_SUPER_ADMIN);
                roleMapper.insert(rolePO);

                UserRolePO userRolePO = new UserRolePO();
                userRolePO.setUserId(userPO.getId());
                userRolePO.setRoleId(rolePO.getId());
                userRoleMapper.insert(userRolePO);
                log.info("初始化超级管理员成功！username={}  password={}", appDbConfigProperties.getSuperAdminUsername(), appDbConfigProperties.getSuperAdminPassword());
                sqlSession.commit();
            }
}
    }
}
