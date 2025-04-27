package com.example.demo.api.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author martix
 * @description
 * @time 2025/4/27 22:51
 */
@Data
public class UserLoginResponse {
    @Schema(description = "CSRF-TOKEN，后续需要放在请求头X-CSRF-TOKEN")
    private String csrfToken;
}
