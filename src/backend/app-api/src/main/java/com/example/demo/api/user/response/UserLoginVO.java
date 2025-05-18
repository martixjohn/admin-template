package com.example.demo.api.user.response;

import com.example.demo.common.pojo.dto.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author martix
 * @description
 * @time 2025/4/27 22:51
 */
@Getter
@Setter
public class UserLoginVO extends UserSafeVO {
//    @Schema(description = "CSRF-TOKEN，后续需要放在请求头X-CSRF-TOKEN", requiredMode = Schema.RequiredMode.REQUIRED)
//    private String csrfToken;

    @Schema(description = "TOKEN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    public UserLoginVO() {}

    public static UserLoginVO of(User user, String token) {
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        userLoginVO.setToken(token);
        userLoginVO.setAvatar(user.getAvatarServerPath());
        return userLoginVO;
    }
}
