package com.loopin.loopinbackend.domain.user.dto.response;

import com.loopin.loopinbackend.domain.user.entity.User;
import com.loopin.loopinbackend.domain.user.enums.Gender;
import com.loopin.loopinbackend.domain.user.enums.Provider;
import com.loopin.loopinbackend.domain.user.enums.Role;
import com.loopin.loopinbackend.domain.user.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "UserInfoResponse", description = "유저 정보 응답 DTO")
public class UserInfoResponse {

    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "이메일", example = "test@loopin.com")
    private String email;

    @Schema(description = "이름", example = "루핀")
    private String firstName;

    @Schema(description = "성", example = "김")
    private String lastName;

    @Schema(description = "닉네임", example = "loopin")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "프로필 사진 URL", example = "https://cdn.example.com/avatar.jpg")
    private String profileImageUrl;

    @Schema(description = "자기소개", example = "저는 루핀입니다.")
    private String bio;

    @Schema(description = "권한", example = "USER")
    private Role role;

    @Schema(description = "상태", example = "ACTIVE")
    private Status status;

    @Schema(description = "이메일 인증 여부", example = "true")
    private boolean emailVerified;

    @Schema(description = "소셜 로그인 사이트", example = "GOOGLE")
    private Provider provider;

    @Schema(description = "소셜 로그인 사이트 ID", example = "11703847562938475629")
    private String providerId;

    @Schema(description = "마지막 로그인 시간", example = "2025-05-19T14:30:00", type = "string", format = "date-time")
    private LocalDateTime lastLoginAt;

    @Schema(description = "생년월일", example = "2000-01-01", type = "string", format = "date")
    private LocalDate birth;

    @Schema(description = "계정 생성일", example = "2025-01-01T09:00:00", type = "string", format = "date-time")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 수정일", example = "2025-05-20T12:30:00", type = "string", format = "date-time")
    private LocalDateTime updatedAt;

    public static UserInfoResponse of(User user) {
        return UserInfoResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .role(user.getRole())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .provider(user.getProvider())
                .providerId(user.getProviderId())
                .lastLoginAt(user.getLastLoginAt())
                .birth(user.getBirth())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
