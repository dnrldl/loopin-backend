package com.loopin.loopinbackend.domain.post.service.query;

import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;
import com.loopin.loopinbackend.domain.post.dto.response.PostDetailResponse;
import com.loopin.loopinbackend.domain.post.qeury.PostSearchCond;
import com.loopin.loopinbackend.global.response.PageResponse;

import java.util.List;

public interface PostQueryService {
    PostDetailResponse getPostInfo(Long postId, Long userId);

    PageResponse<PostDetailResponse> getPosts(PostSearchCond condition, Long userId);

    List<CommentResponse> getCommentTree(Long postId);
}
