package com.example.demo.api.debug.controller;

import com.example.demo.common.annotation.OnDebugMode;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author martix
 * @description
 * @time 2025/5/9 18:40
 */
@Controller
@RequestMapping("/debug")
@RequiredArgsConstructor
@Tag(name = "debug", description = "debug使用，仅在开发和测试使用")
@OnDebugMode
public class DebugController {

    private boolean isDevMode = false;

    @Autowired
    public void setEnvironment(Environment env) {
        for (String activeProfile : env.getActiveProfiles()) {
            if (activeProfile.equalsIgnoreCase("dev")) {
                isDevMode = true;
                break;
            }
        }
    }

    @Operation(summary = "返回对应状态码的响应")
    @GetMapping("/status")
    public ResponseEntity<?> status(int status) {
        checkDevEnvironment();
        return ResponseEntity.status(status).build();
    }

    private void checkDevEnvironment() {
        if (!isDevMode) {
            throw new AppServiceException(ExceptionCode.BAD_REQUEST);
        }
    }

}
