package com.example.demo.common.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.demo.common.pojo.service.User;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 字段填充策略
 * @author martix
 * @description https://baomidou.com/guides/auto-fill-field/
 * @time 2025/4/26
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if(metaObject.hasGetter("createdTime")) {
            strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        }
        if(metaObject.hasGetter("createdBy")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null &&  authentication.getPrincipal() instanceof User user) {
                strictInsertFill(metaObject, "createdBy", Long.class, user.getId());
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(metaObject.hasGetter("updatedTime")) {
            strictInsertFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
        }
        if(metaObject.hasGetter("updatedBy")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication != null &&  authentication.getPrincipal() instanceof User user) {
                strictInsertFill(metaObject, "updatedBy", Long.class, user.getId());
            }
        }
    }
}
