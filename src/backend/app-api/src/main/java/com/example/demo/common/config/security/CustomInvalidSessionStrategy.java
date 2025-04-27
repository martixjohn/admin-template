package com.example.demo.common.config.security;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;

/**
 * @author martix
 * @description
 * @time 2025/4/27 9:37
 */
@Slf4j
@Component
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("检测到无效session");
        ResponseUtil.clearAllSiteData(request, response);

        ResponseUtil.writeJSONWithDefaultEncoding(response,
                ApiResponse.failure(ExceptionCode.BAD_REQUEST, "无效session"));

    }

}
