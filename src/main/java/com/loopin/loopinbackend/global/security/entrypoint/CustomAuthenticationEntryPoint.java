package com.loopin.loopinbackend.global.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopin.loopinbackend.global.error.ErrorCode;
import com.loopin.loopinbackend.global.response.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn("401 Unauthorized access");

        ApiErrorResponse errorResponse = ApiErrorResponse.from(ErrorCode.UNAUTHORIZED);

        response.setStatus(ErrorCode.UNAUTHORIZED.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }
}