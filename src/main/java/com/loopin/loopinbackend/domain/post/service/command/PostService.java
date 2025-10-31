package com.loopin.loopinbackend.domain.post.service.command;

import com.loopin.loopinbackend.domain.post.dto.request.PostCreateRequest;
import com.loopin.loopinbackend.domain.post.dto.request.PostUpdateRequest;

public interface PostService {
    Long createPost(PostCreateRequest request, Long userId);
    void updatePost(PostUpdateRequest request, Long postId, Long userId);
    void deletePost(Long postId, Long userId);
    void createPosts();


}
