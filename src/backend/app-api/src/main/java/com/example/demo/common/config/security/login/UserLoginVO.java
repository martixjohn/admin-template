package com.example.demo.common.config.security.login;

import com.example.demo.api.user.response.UserSafeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author martix
 * @description
 * @time 2025/4/27 22:51
 */
@Getter
@Setter
public class UserLoginVO {
    @Schema(description = "CSRF-TOKEN，后续需要放在请求头X-CSRF-TOKEN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String csrfToken;
}
