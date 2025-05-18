package com.example.demo.common.response;

import com.example.demo.common.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回响应体
 *
 * @param <T> data字段类型
 * @author martix
 * @description 主要以JSON形式序列化
 * @time 2025/4/21 22:12
 */
@Getter
@Setter
public class ApiResponse<T> {

    @Schema(description = "错误码", requiredMode = Schema.RequiredMode.REQUIRED)
    private final int code;

    @Schema(description = "错误信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String msg;

    @Schema(description = "实际数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final T data;

    protected ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //    @Schema(description = "是否成功", requiredMode = Schema.RequiredMode.REQUIRED)
//    public boolean isSuccess() {
//        return code == ExceptionCode.SUCCESS.getCode();
//    }

    public static <T> ApiResponse<T> success(T data) {
        ExceptionCode success = ExceptionCode.SUCCESS;
        return new ApiResponse<>(success.getCode(), success.getMsg(), data);
    }

    public static <T> ApiResponse<T> success(T data, String msg) {
        ExceptionCode success = ExceptionCode.SUCCESS;
        return new ApiResponse<>(success.getCode(), msg, data);
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> failure(ExceptionCode expCode) {
        return new ApiResponse<>(expCode.getCode(), expCode.getMsg(), null);
    }

    public static ApiResponse<Void> failure(ExceptionCode expCode, String msg) {
        return new ApiResponse<>(expCode.getCode(), msg, null);
    }

    public static <T> ApiResponse<T> failure(ExceptionCode expCode, String msg, T data) {
        return new ApiResponse<>(expCode.getCode(), msg, data);
    }

}
