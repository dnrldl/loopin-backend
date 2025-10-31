package com.loopin.loopinbackend.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static com.loopin.loopinbackend.global.constant.ValidationConstant.*;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "UserLoginRequest", description = "유저 로그인 요청 DTO")
@ToString
public class UserLoginRequest {
        @Schema(description = "이메일", example = "loopin@loopin.com")
        @NotBlank(message = REQUIRED_EMAIL)
        @Email(message = EMAIL_MESSAGE)
        @Pattern(
                regexp = EMAIL_REGEXP,
                message = EMAIL_PATTERN
        )
        String email;

        @Schema(description = "비밀번호", example = "loopin1234!")
        @NotBlank(message = REQUIRED_PASSWORD)
        @Size(min = 8, max = 20, message = PASSWORD_SIZE)
        @Pattern(
                regexp = PASSWORD_REGEXP,
                message = PASSWORD_PATTERN
        )
        String password;
}
