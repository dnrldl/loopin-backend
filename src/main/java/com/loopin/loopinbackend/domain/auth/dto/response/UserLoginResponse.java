package com.loopin.loopinbackend.domain.auth.dto.response;

import com.loopin.loopinbackend.domain.user.entity.User;
import com.loopin.loopinbackend.domain.user.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "UserLoginResponse", description = "유저 로그인 응답 DTO")
public class UserLoginResponse {
        @Schema(description = "액세스 토큰", example = "ca23e05ead1f753fae9d4791f508925812394c8f9e9da261627a89c5a3a4d0d8")
        String accessToken;

        @Schema(description = "유저 ID", example = "1")
        Long userId;

        @Schema(description = "이메일", example = "test@loopin.com")
        private String email;

        @Schema(description = "닉네임", example = "loopin")
        private String nickname;

        @Schema(description = "프로필 사진 URL", example = "https://cdn.example.com/avatar.jpg")
        private String profileImageUrl;

        @Schema(description = "권한", example = "USER")
        private String role;

        public static UserLoginResponse of(User user, String accessToken) {
                return UserLoginResponse.builder()
                        .accessToken(accessToken)
                        .userId(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .role(user.getRole().name())
                        .build();
        }
}
