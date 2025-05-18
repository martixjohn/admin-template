package com.example.demo.api.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.api.user.request.UserChangePasswordRequest;
import com.example.demo.api.user.request.UserRegisterRequest;
import com.example.demo.api.user.response.UserSafeVO;
import com.example.demo.api.user.request.UserAddRequest;
import com.example.demo.common.annotation.PermitAllAuthorization;
import com.example.demo.common.auth.Authorities;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.pojo.dto.User;
import com.example.demo.common.request.PageQueryRequest;
import com.example.demo.common.response.ApiPageResponse;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Tag(name = "user", description = "用户管理")
public class UserController {
    private final UserService userService;

    @PermitAllAuthorization
    @Operation(summary = "用户自行注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody @Validated UserRegisterRequest request) {
        String username = request.username();
        String password = request.password();
        userService.checkValidUsernameAndPassword(username, password);
        userService.addUser(username, password, Authorities.ROLE_USER);
        return ApiResponse.success();
    }

    @PermitAllAuthorization
    @Operation(summary = "检查是否登录")
    @GetMapping("/is-login")
    public ApiResponse<Boolean> isLogin(@AuthenticationPrincipal User user) {
        return ApiResponse.success(user != null);
    }

    @Operation(summary = "获取自己的用户信息")
    @GetMapping("/my-info")
    public ApiResponse<UserSafeVO> geCurrentUser(@AuthenticationPrincipal @Validated User user) {
        log.info("user: {}", user);
        UserSafeVO userSafeVO = new UserSafeVO();
        BeanUtils.copyProperties(user, userSafeVO);
        return ApiResponse.success(userSafeVO);
    }


    @PostMapping("/get-batch")
    public ApiPageResponse<UserSafeVO> getBatch(@RequestBody @Validated PageQueryRequest pageQueryRequest) {
        IPage<User> batch = userService.getBatch(pageQueryRequest);
        List<UserSafeVO> records = batch.getRecords().stream().map(UserSafeVO::of).toList();
        return ApiPageResponse.success(batch.getTotal(), batch.getCurrent(), batch.getSize(), records);
    }


    @Secured({Authorities.ROLE_ADMIN})
    @Operation(summary = "管理员添加用户")
    @PostMapping("/add")
    public ApiResponse<Void> addUser(@RequestBody @Validated UserAddRequest registerRequest) {
        userService.addUser(registerRequest.username(), registerRequest.password(), registerRequest.role());
        return ApiResponse.success();
    }

    @Secured({Authorities.ROLE_ADMIN})
    @Operation(summary = "管理员删除用户")
    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteUser(@AuthenticationPrincipal User user) {
        synchronized (user.getUsername().intern()) {
            userService.deleteUser(user.getUsername());
            userService.invalidateAllSessionsOfUser(user.getUsername());
        }
        return ApiResponse.success();
    }

    @Operation(summary = "改变密码")
    @PutMapping("/change-password")
    public ApiResponse<Void> changePassword(@RequestBody @Validated UserChangePasswordRequest changePasswordRequest,
                                            @AuthenticationPrincipal User user) {
        String username = user.getUsername();
        String oldPassword = changePasswordRequest.oldPassword();

        if (!userService.checkPassword(username, oldPassword)) {
            return ApiResponse.failure(ExceptionCode.FORBIDDEN, "密码错误");
        }
        synchronized (user.getUsername().intern()) {
            userService.changePassword(username, changePasswordRequest.newPassword());
            userService.invalidateAllSessionsOfUser(username);
        }
        return ApiResponse.success();
    }

}
