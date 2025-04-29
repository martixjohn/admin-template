package com.example.demo.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 返回工具类
 *
 * @author martix
 * @description
 * @time 2025/4/27 18:20
 */
@Component// 为了注入属性
public class ResponseUtil {

    /**
     * 使用应用配置的默认编码，写入JSON到响应体
     *
     * @param response 标准HttpServletResponse
     * @param obj      需要序列化的object
     * @throws IOException IO异常
     */
    public static void writeJSONWithDefaultEncoding(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(ENCODING);
        OBJECT_MAPPER.writeValue(response.getWriter(), obj);
    }


    private ResponseUtil(ObjectMapper objectMapper, @Value("${app.common.charset}") String encoding) {
        synchronized (ResponseUtil.class) {
            OBJECT_MAPPER = objectMapper;
            ENCODING = encoding;
        }
    }


    private static ObjectMapper OBJECT_MAPPER = null;

    private static String ENCODING = null;
}
