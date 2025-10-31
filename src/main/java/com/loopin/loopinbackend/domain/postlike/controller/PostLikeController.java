package com.loopin.loopinbackend.domain.postlike.controller;

import com.loopin.loopinbackend.global.security.util.SecurityUtils;
import com.loopin.loopinbackend.domain.postlike.service.command.PostLikeService;
import com.loopin.loopinbackend.global.response.ApiErrorResponse;
import com.loopin.loopinbackend.global.response.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post Like", description = "게시글(댓글) 좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    // private
    @Operation(summary = "좋아요 생성",
            description = "로그인한 사용자의 ID, 게시글의 ID로 좋아요을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiSuccessResponse<Void>> likePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getCurrentUser().getId();
        postLikeService.like(postId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiSuccessResponse.of(null));
    }

    @Operation(summary = "좋아요 삭제",
            description = "로그인한 사용자의 ID, 게시글의 ID로 좋아요을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/{postId}/unlike")
    public ResponseEntity<ApiSuccessResponse<Void>> unlikePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getCurrentUser().getId();
        postLikeService.unlike(postId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiSuccessResponse.of(null));
    }
}
