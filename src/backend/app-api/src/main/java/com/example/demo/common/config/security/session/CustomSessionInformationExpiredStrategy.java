package com.example.demo.common.config.security.session;

import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.common.response.ApiResponse;
import com.example.demo.common.util.ResponseUtil;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Session过期处理
 *
 * @author martix
 * @description
 * @time 2025/4/28
 */
@Slf4j
@Component
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        SessionInformation sessionInformation = event.getSessionInformation();
        log.info("Session过期：{}", sessionInformation);
        ResponseUtil.writeJSONWithDefaultEncoding(event.getResponse(), ApiResponse.failure(ExceptionCode.FORBIDDEN, "会话已过期，请重新登录"));
    }
}
