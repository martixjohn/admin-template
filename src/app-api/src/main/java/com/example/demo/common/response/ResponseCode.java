package com.example.demo.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author martix
 * @description
 * @time 2025/4/21 22:14
 */
@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "success"),
    FAILURE(500, "failure"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    ;
    private final int code;
    private final String msg;

}
