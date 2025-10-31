package com.loopin.loopinbackend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "TokensDto", description = "access, refresh 토큰 DTO")
@ToString
public class TokensDto {
    private String accessToken;
    private String refreshToken;
}
