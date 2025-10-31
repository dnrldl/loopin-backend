package com.loopin.loopinbackend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ImageRegisterRequest(
        @Schema(description = "s3 스토리지에 저장된 객체키", example = "images/post/8f23a7ac-6e0a-4f4c-b234.jpg")
        String storageKey,
        @Schema(description = "클라이언트가 직접 접근할 주소", example = "https://cdn.example.com/images/post/2025/08/8f23a7ac-6e0a-4f4c-b234.jpg")
        String url,
        @Schema(description = "이미지 가로 픽셀", example = "1200")
        Integer width,
        @Schema(description = "이미지 세로 픽셀", example = "1200")
        Integer height,
        @Schema(description = "이미지 파일 크기", example = "245678")
        Long bytes,
        @Schema(description = "이미지 MIME 타입", example = "image/jpeg")
        String mimeType
) {}

