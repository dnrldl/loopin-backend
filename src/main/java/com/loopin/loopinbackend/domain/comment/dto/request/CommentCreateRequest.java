package com.loopin.loopinbackend.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "CommentCreateRequest", description = "댓글 생성 요청 DTO")
public class CommentCreateRequest {
    @Schema(description = "내용", example = "댓글 내용")
    private String content;
}
