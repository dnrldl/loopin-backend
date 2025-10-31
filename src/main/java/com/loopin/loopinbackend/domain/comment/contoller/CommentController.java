package com.loopin.loopinbackend.domain.comment.contoller;

import com.loopin.loopinbackend.domain.auth.model.CustomUserDetails;
import com.loopin.loopinbackend.global.security.util.SecurityUtils;
import com.loopin.loopinbackend.domain.comment.dto.request.CommentCreateRequest;
import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;
import com.loopin.loopinbackend.domain.comment.service.command.CommentService;
import com.loopin.loopinbackend.domain.comment.service.query.CommentQueryService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentQueryService commentQueryService;
    private final CommentService commentService;
    @Operation(summary = "댓글 복수건 조회",
            description = "게시글 ID를 이용해서 댓글 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "조회 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
    })
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<CommentResponse>>> getComments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId
    ) {
        Long userId = null;
        if (userDetails != null) userId = userDetails.getUserId();

        List<CommentResponse> comments = commentQueryService.getComments(postId, userId);
        return ResponseEntity.ok(ApiSuccessResponse.of(comments));
    }

    // private
    @Operation(summary = "댓글 생성",
            description = "로그인한 사용자의 ID, 게시글 내용으로 댓글을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<Long>> createComment(@RequestBody CommentCreateRequest request, @PathVariable Long postId) {
        Long currentUserId = SecurityUtils.getCurrentUser().getId();

        Long commentId = commentService.createComment(request, postId, currentUserId);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccessResponse.of(commentId));
    }
}
