package com.loopin.loopinbackend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PostCreateRequest", description = "게시글 생성 요청 DTO")
public class PostCreateRequest {
    @Schema(description = "내용", example = "게시글 내용")
    private String content;
}
