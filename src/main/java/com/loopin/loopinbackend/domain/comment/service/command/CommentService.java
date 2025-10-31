package com.loopin.loopinbackend.domain.comment.service.command;

import com.loopin.loopinbackend.domain.comment.dto.request.CommentCreateRequest;

public interface CommentService {
    Long createComment(CommentCreateRequest request, Long postId, Long userId);
}
