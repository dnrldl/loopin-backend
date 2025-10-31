package com.loopin.loopinbackend.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("E001", "알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // global
    NOT_FOUND("G001", "요청하신 URL을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("G002", "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),

    // user
    DUPLICATE_EMAIL("U001", "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_NICKNAME("U002", "이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("U003", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
    INVALID_INPUT_VALUE("U004", "요청 입력 형식이 맞지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("U005", "기존 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_SAME_AS_OLD("U006", "새 비밀번호는 기존 비밀번호와 같을 수 없습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_PHONE_NUMBER("U007", "이미 사용 중인 전화번호입니다.", HttpStatus.BAD_REQUEST),

    // post
    POST_NOT_FOUND("P001", "게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_NOT_OWNER("P002", "게시글의 작성자가 아닙니다.", HttpStatus.FORBIDDEN),

    // post_like
    POST_LIKE_NOT_FOUND("PL001", "현재 유저의 해당 게시글 좋아요 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_ALREADY_LIKED("PL002", "현재 유저가 해당 게시글을 이미 좋아요 했습니다.", HttpStatus.BAD_REQUEST),
    POST_NOT_LIKED("PL003", "현재 유저가 해당 게시글을 좋아요 하지 않았습니다.", HttpStatus.BAD_REQUEST),

    // auth
    INVALID_LOGIN("A001", "자격 증명에 실패하였습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("A002", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("A003", "접근 권한이 필요합니다.", HttpStatus.FORBIDDEN),

    // jwt (auth)
    INVALID_JWT("J001", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT("J002", "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    JWT_VALIDATION_ERROR("J003", "JWT 검증 중 예기치 못한 오류가 발생했습니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_JWT("J004", "토큰이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    BLACKLISTED_TOKEN("J005", "토큰이 블랙리스트에 있습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("J006", "유효하지 않은 refresh 토큰입니다.", HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
