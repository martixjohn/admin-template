package com.example.demo.common.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.demo.common.pojo.service.User;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 字段填充策略
 * @author martix
 * @description https://baomidou.com/guides/auto-fill-field/
 * @time 2025/4/26
 */
@Component
@RequiredArgsConstructor
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasGetter("createdTime")) {
            strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        }
        if(metaObject.hasGetter("createdBy")) {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            if(authentication != null &&  authentication.getPrincipal() instanceof User user) {
                strictInsertFill(metaObject, "createdBy", Long.class, user.getId());
            }
        }
        strictInsertFill(metaObject, "uuid", UUID.class, UUID.randomUUID());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasGetter("updatedTime")) {
            strictInsertFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
        }
        if(metaObject.hasGetter("updatedBy")) {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            if(authentication != null &&  authentication.getPrincipal() instanceof User user) {
                strictInsertFill(metaObject, "updatedBy", Long.class, user.getId());
            }
        }
    }
}
