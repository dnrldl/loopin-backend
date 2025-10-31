package com.loopin.loopinbackend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PostUpdateRequest", description = "게시글 변경 요청 DTO")
public class PostUpdateRequest {
    @Schema(description = "내용", example = "게시글 내용")
    private String content;
}
