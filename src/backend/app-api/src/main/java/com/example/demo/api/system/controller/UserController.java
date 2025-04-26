package com.example.demo.api.system.controller;

import com.example.demo.api.system.response.UserSafeVO;
import com.example.demo.common.exception.AppServiceException;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.pojo.service.User;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 用户管理控制器
 *
 * @author martix
 * @description
 * @time 2025/4/21 22:31
 */
@Slf4j
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
@Tag(name = "用户管理")
public class UserController {
    private final UserService userService;

    @Operation(description = "获取自己的用户信息")
    @GetMapping("/info")
    public ApiResponse<UserSafeVO> getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        log.info("principal {}", principal);
        if (principal instanceof UserDetails userDetails) {
            Optional<User> user = userService.getFullInfoByUsername(userDetails.getUsername());
            return user.map(u -> {
                UserSafeVO userSafeVO = new UserSafeVO();
                BeanUtils.copyProperties(u, userSafeVO);
                return ApiResponse.success(userSafeVO);
            }).orElseThrow(
                    () -> new AppServiceException(ExceptionCode.INTERNAL_SERVER_ERROR)
            );
        }
        // 视为出错，principal只能是UserDetails
        throw new AppServiceException(ExceptionCode.INTERNAL_SERVER_ERROR);
    }
}
