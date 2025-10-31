package com.loopin.loopinbackend.domain.comment.repository.query;

import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;

import java.util.List;

public interface CommentQueryRepository {
    List<CommentResponse> findComments(Long postId);
}
