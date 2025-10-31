package com.loopin.loopinbackend.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.loopin.loopinbackend.domain.user.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.loopin.loopinbackend.global.constant.ValidationConstant.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserRegisterRequest", description = "유저 회원가입 요청 DTO")
@ToString
public class UserRegisterRequest {

    @Schema(description = "이메일", example = "loopin@loopin.com")
    @NotBlank(message = REQUIRED_EMAIL)
    @Email(message = EMAIL_MESSAGE)
    @Pattern(
            regexp = EMAIL_REGEXP,
            message = EMAIL_PATTERN
    )
    private String email;

    @Schema(description = "비밀번호", example = "loopin1234!")
    @NotBlank(message = REQUIRED_PASSWORD)
    @Size(min = 8, max = 20, message = PASSWORD_SIZE)
    @Pattern(
            regexp = PASSWORD_REGEXP,
            message = PASSWORD_PATTERN
    )
    private String password;

    @Schema(description = "닉네임", example = "looper")
    @NotBlank(message = REQUIRED_NICKNAME)
    @Size(min = 2, max = 20, message = NICKNAME_SIZE)
    private String nickname;

    @Schema(description = "이름", example = "루핀")
    @NotBlank(message = REQUIRED_FIRST_NAME)
    @Size(max = 10, message = FIRST_NAME_SIZE)
    private String firstName;

    @Schema(description = "성", example = "김")
    @NotBlank(message = REQUIRED_LAST_NAME)
    @Size(max = 10, message = LAST_NAME_SIZE)
    private String lastName;

    @Schema(description = "전화번호", example = "01012345678")
    @NotBlank(message = REQUIRED_PHONE_NUMBER)
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = PHONE_NUMBER_PATTERN
    )
    private String phoneNumber;

    @Schema(description = "성별", allowableValues = {"MALE", "FEMALE", "OTHER"}, example = "MALE")
    @NotNull(message = REQUIRED_GENDER)
    private Gender gender;

    @Schema(description = "생년월일", example = "2000-01-01")
    @NotNull(message = REQUIRED_BIRTH)
    @Past(message = BIRTH_PAST_MESSAGE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
}