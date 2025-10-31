package com.loopin.loopinbackend.domain.comment.service.query;

import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;

import java.util.List;

public interface CommentQueryService {
    List<CommentResponse> getComments(Long postId, Long userId);
}
