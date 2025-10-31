package com.loopin.loopinbackend.global.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopin.loopinbackend.global.error.ErrorCode;
import com.loopin.loopinbackend.global.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ApiErrorResponse errorResponse = ApiErrorResponse.from(ErrorCode.UNAUTHORIZED);

        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        // 선택: 브라우저/클라이언트에게 Bearer 인증 체계 알림
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");

        // 전역 설정이 적용된 ObjectMapper로 바로 쓰기 (LocalDateTime 직렬화 OK)
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}