package com.example.demo.api.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martix
 * @description
 * @time 2025/5/9 14:55
 */
@RestController
@RequestMapping("/system/runtime-info")
@Tag(name = "runtime-info", description = "系统运行情况")
public class RuntimeInfoController {
}
