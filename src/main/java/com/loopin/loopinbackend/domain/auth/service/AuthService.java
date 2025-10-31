package com.loopin.loopinbackend.domain.auth.service;

import com.loopin.loopinbackend.domain.auth.dto.LoginResponse;
import com.loopin.loopinbackend.domain.auth.dto.TokensDto;
import com.loopin.loopinbackend.domain.auth.dto.request.UserLoginRequest;
import com.loopin.loopinbackend.domain.auth.dto.response.UserLoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponse login (UserLoginRequest request);

    void logout (HttpServletRequest request);

    TokensDto refreshToken (String refreshToken);

    UserLoginResponse me (Long userId);
}
