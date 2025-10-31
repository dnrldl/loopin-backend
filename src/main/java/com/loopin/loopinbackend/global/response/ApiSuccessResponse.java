package com.loopin.loopinbackend.global.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ApiResponse", description = "공통 성공 응답 래퍼")
public record ApiSuccessResponse<T>(
        @Schema(description = "요청 성공 여부", example = "true")
        boolean success,

        @Schema(description = "상태코드", example = "200")
        Integer status,

        @Schema(description = "응답 데이터")
        T data,

        @Schema(description = "API 응답 시간", example = "2025-05-21T13:50:00" , type = "string", format = "date-time")
        LocalDateTime timestamp
) {
    public static <T> ApiSuccessResponse<T> of(T data, Integer status) {
        return new ApiSuccessResponse<>(true, status, data, LocalDateTime.now());
    }

    public static <T> ApiSuccessResponse<T> of(T data) {
        return new ApiSuccessResponse<>(true, 200, data, LocalDateTime.now());
    }

    public static <T> ApiSuccessResponse<T> fail(String message) { return new ApiSuccessResponse<>(false, 400, null, LocalDateTime.now()); }
}
