package com.loopin.loopinbackend.domain.media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PresignRequest(
        @NotBlank String usage,       // POST_IMAGE, USER_AVATAR ...
        String pathPrefix,            // optional; 정책에서 강제 prefix가 있으면 덮어씀
        @NotEmpty @Size(max = 50) List<File> files
) {
    public record File(
            @NotBlank String originalFileName,
            Long sizeBytes
    ) {}
}