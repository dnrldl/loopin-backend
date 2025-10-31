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
@Schema(name = "UserDeleteRequest", description = "유저 탈퇴 요청 DTO")
public class UserDeleteRequest {
    @Schema(description = "비밀번호", example = "loopin1234!")
    @NotBlank(message = REQUIRED_PASSWORD)
    @Size(min = 8, max = 20, message = PASSWORD_SIZE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_PATTERN
    )
    private String password;
}
