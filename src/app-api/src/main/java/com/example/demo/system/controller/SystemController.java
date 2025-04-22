package com.example.demo.system.controller;

import com.example.demo.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martix
 * @description
 * @time 2025/4/22 20:29
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    
    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success();
    }
}
