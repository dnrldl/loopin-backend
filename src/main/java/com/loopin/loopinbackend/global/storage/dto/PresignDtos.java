package com.loopin.loopinbackend.global.storage.dto;

import lombok.Builder;

import java.util.List;

public class PresignDtos {
    @Builder
    public record FileSpec(
            String originalFileName,
            Long sizeBytes
    ) {}

    @Builder
    public record PresignUrlDetail(
            String key,              // S3 object key (ex: images/post/2025/08/uuid.jpg)
            String presignedUrl,     // PUT용 사전서명 URL
            String fileUrl,          // CDN/S3 공개 URL (읽기용)
            String contentType,      // 업로드 시 반드시 사용
            String requiredHeaderOriginalName // "x-amz-meta-original-filename" 값
    ) {}

}
