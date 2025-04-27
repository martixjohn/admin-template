package com.example.demo.common.util;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    /**
     * 清除所有Cookie和站点数据
     *
     * @param request  请求
     * @param response 响应
     */
    public static void clearAllSiteData(HttpServletRequest request, HttpServletResponse response) {
        assert request != null;
        assert response != null;
        /*清除cookie*/
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie == null) continue;
            cookie.setPath("/");
            cookie.setMaxAge(0);
//            clearCookie.setSecure(request.isSecure());
            response.addCookie(cookie);
        }
        /*设置Clear-Site-Data*/
        clearSiteDataHeaderWriter.writeHeaders(request, response);
    }


    private ResponseUtil(ObjectMapper objectMapper, @Value("${app.common.charset}") String encoding) {
        synchronized (this) {
            OBJECT_MAPPER = objectMapper;
            ENCODING = encoding;
        }
    }

    private final static ClearSiteDataHeaderWriter clearSiteDataHeaderWriter = new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL);

    private static ObjectMapper OBJECT_MAPPER = null;

    private static String ENCODING = null;
}
