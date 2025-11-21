package com.loopin.loopinbackend.domain.auth.dto;

import com.loopin.loopinbackend.domain.auth.dto.response.UserLoginResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private final String refreshToken;
    private final UserLoginResponse userLoginResponse;
}