package com.loopin.loopinbackend.global.constant;

public final class ValidationConstant {
    // NotBlank
    public static final String REQUIRED_EMAIL = "이메일은 필수 입력값입니다.";
    public static final String REQUIRED_PASSWORD = "비밀번호는 필수 입력값입니다.";
    public static final String REQUIRED_NICKNAME = "닉네임은 필수 입력값입니다.";
    public static final String REQUIRED_FIRST_NAME = "이름은 필수 입력값입니다.";
    public static final String REQUIRED_LAST_NAME = "성은 필수 입력값입니다.";
    public static final String REQUIRED_PHONE_NUMBER = "전화번호는 필수 입력값입니다.";
    public static final String REQUIRED_GENDER = "성별는 필수 입력값입니다.";
    public static final String REQUIRED_BIRTH = "생년월일는 필수 입력값입니다.";

    // Size
    public static final String PASSWORD_SIZE = "비밀번호는 8~20자 사이여야 합니다.";
    public static final String NICKNAME_SIZE = "닉네임은 2~20자여야 합니다.";
    public static final String FIRST_NAME_SIZE = "이름은 최대 10자입니다.";
    public static final String LAST_NAME_SIZE = "성은 최대 10자입니다.";

    // Pattern
    public static final String PASSWORD_PATTERN = "비밀번호는 대문자·소문자·숫자·특수문자를 모두 포함해야 합니다.";
    public static final String EMAIL_PATTERN = "이메일은 example@domain.com 형식이어야 합니다.";
    public static final String PHONE_NUMBER_PATTERN = "전화번호는 10~11자리 숫자여야 합니다.";

    // Pattern Regexp
    public static final String EMAIL_REGEXP = "^[\\w-.]+@[\\w-]+\\.[A-Za-z]{2,}$";
    public static final String PASSWORD_REGEXP = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).+$";

    // Email
    public static final String EMAIL_MESSAGE = "유효한 이메일 형식이어야 합니다.";

    // Past
    public static final String BIRTH_PAST_MESSAGE = "과거의 날짜이어야 합니다.";

    public ValidationConstant() {
    }
}