package com.loopin.loopinbackend.domain.postlike.service.command;

public interface PostLikeService {
    boolean like(Long postId, Long userId);
    boolean unlike(Long postId, Long userId);
}
