package com.example.demo.api.user.controller;

import com.example.demo.api.user.request.UserChangePasswordRequest;
import com.example.demo.api.user.response.UserSafeVO;
import com.example.demo.api.user.request.UserAddRequest;
import com.example.demo.common.annotation.PermitAllAuthentication;
import com.example.demo.common.auth.Authorities;
import com.example.demo.common.pojo.service.User;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * @author martix
 * @description
 * @time 2025/4/21 22:31
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "用户管理")
public class UserController {
    private final UserService userService;
    private final LogoutHandler logoutHandler;

    @Operation(summary = "检查是否登录")
    @PermitAllAuthentication
    @GetMapping("/is-login")
    public ApiResponse<Boolean> isLogin(@AuthenticationPrincipal User user) {
        return ApiResponse.success(user != null);
    }

    @Operation(summary = "获取自己的用户信息")
    @GetMapping("/my-info")
    public ApiResponse<UserSafeVO> geCurrentUser(@AuthenticationPrincipal User user) {
        log.info("user: {}", user);
        UserSafeVO userSafeVO = new UserSafeVO();
        BeanUtils.copyProperties(user, userSafeVO);
        return ApiResponse.success(userSafeVO);
    }


    @Secured({Authorities.ROLE_ADMIN})
    @PostMapping("/add")
    public ApiResponse<Void> addUser(@RequestBody UserAddRequest registerRequest) {
        userService.addUser(registerRequest.username(), registerRequest.password(), registerRequest.role());
        return ApiResponse.success();
    }

    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody UserChangePasswordRequest changePasswordRequest,
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) {
        userService.changePassword(changePasswordRequest.oldPassword(), changePasswordRequest.newPassword());
        // 所有都下线
        logoutHandler.logout(request, response, authentication);
        return ApiResponse.success();
    }

//    public ApiResponse<List<UserSafeVO>> getUsers() {
//
//    }
}
