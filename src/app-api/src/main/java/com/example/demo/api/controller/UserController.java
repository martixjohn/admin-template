package com.example.demo.user.controller;

import com.example.demo.common.response.ApiResponse;
import com.example.demo.user.pojo.entity.UserPO;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:31
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/id/{id}")
    @Secured("ROLE_ADMIN")
    public ApiResponse<UserPO> getUserById(@PathVariable Integer id) {

        return ApiResponse.success(userService.getById(id));
    }
}
