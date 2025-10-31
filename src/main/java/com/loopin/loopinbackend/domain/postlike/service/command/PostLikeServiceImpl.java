package com.loopin.loopinbackend.domain.postlike.service.command;

import com.loopin.loopinbackend.domain.post.repository.PostJpaRepository;
import com.loopin.loopinbackend.domain.postlike.entity.PostLike;
import com.loopin.loopinbackend.domain.postlike.exception.AlreadyLikedException;
import com.loopin.loopinbackend.domain.postlike.exception.PostLikeNotFoundException;
import com.loopin.loopinbackend.domain.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostJpaRepository postJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean like(Long postId, Long userId) {
//        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) throw new AlreadyLikedException();
        String key = "post:" + postId + ":likes";

        postLikeRepository.save(new PostLike(postId, userId));
        postJpaRepository.incrementLikeCount(postId);

        try {
            redisTemplate.opsForSet().add(key, userId);
        } catch (Exception e) {
            log.warn("Redis 업데이트 실패: postId={}, userId={}", postId, userId);
        }
        return false;
    }

    @Override
    public boolean unlike(Long postId, Long userId) {
//        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) throw new PostLikeNotFoundException();
        String key = "post:" + postId + ":likes";

        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
        postJpaRepository.decrementLikeCount(postId);

        try {
            redisTemplate.opsForSet().remove(key, userId);
        } catch (Exception e) {
            log.warn("Redis 업데이트 실패: postId={}, userId={}", postId, userId);
        }
        return false;
    }
}
