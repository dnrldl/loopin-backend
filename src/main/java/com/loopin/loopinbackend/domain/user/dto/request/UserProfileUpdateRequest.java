package com.loopin.loopinbackend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.loopin.loopinbackend.global.constant.ValidationConstant.NICKNAME_SIZE;
import static com.loopin.loopinbackend.global.constant.ValidationConstant.REQUIRED_NICKNAME;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserProfileUpdateRequest", description = "유저 회원정보 변경 요청 DTO")
public class UserProfileUpdateRequest {
    @Schema(description = "닉네임", example = "looper")
    @NotBlank(message = REQUIRED_NICKNAME)
    @Size(min = 2, max = 20, message = NICKNAME_SIZE)
    private String nickname;

    @Schema(description = "프로필 이미지 URL")
    private String profileImageUrl;

    @Schema(description = "프로필 자기소개")
    private String bio;
}
