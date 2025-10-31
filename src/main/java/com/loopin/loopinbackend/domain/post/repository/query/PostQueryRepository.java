package com.loopin.loopinbackend.domain.post.repository.query;

import com.loopin.loopinbackend.domain.post.dto.FlatCommentDto;
import com.loopin.loopinbackend.domain.post.dto.response.PostDetailResponse;
import com.loopin.loopinbackend.domain.post.qeury.PostSearchCond;

import java.util.List;

public interface PostQueryRepository {
    PostDetailResponse findPostById(Long postId);

    List<PostDetailResponse> findPosts(int offset, PostSearchCond condition, Long userId);

    Long countPosts();

    List<FlatCommentDto> findCommentTreeByPostId(Long postId);
}
