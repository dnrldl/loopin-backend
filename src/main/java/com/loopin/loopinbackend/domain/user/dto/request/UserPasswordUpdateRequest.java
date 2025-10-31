package com.loopin.loopinbackend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.loopin.loopinbackend.global.constant.ValidationConstant.*;

@Getter
@AllArgsConstructor
@Schema(name = "UserPasswordUpdateRequest", description = "유저 비밀번호 변경 요청 DTO")
public class UserPasswordUpdateRequest {
    @Schema(description = "기존 비밀번호", example = "Loopin1234!")
    @NotBlank(message = REQUIRED_PASSWORD)
    @Size(min = 8, max = 20, message = PASSWORD_SIZE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_PATTERN
    )
    private String oldPassword;

    @Schema(description = "변경할 비밀번호", example = "Update1234!")
    @NotBlank(message = REQUIRED_PASSWORD)
    @Size(min = 8, max = 20, message = PASSWORD_SIZE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_PATTERN
    )
    private String newPassword;
}
