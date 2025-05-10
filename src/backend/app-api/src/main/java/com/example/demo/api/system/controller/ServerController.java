package com.example.demo.api.system.controller;

import com.example.demo.common.annotation.PermitAllAuthorization;
import com.example.demo.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martix
 * @description
 * @time 2025/4/22 20:29
 */
@RestController
@RequestMapping("/system/server")
@Tag(name = "server", description = "服务器信息")
public class ServerController {

    @Operation(summary = "服务器健康检查")
    @PermitAllAuthorization
    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success(null, "服务器正常");
    }

}
