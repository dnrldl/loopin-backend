package com.loopin.loopinbackend.domain.post.repository;

import com.loopin.loopinbackend.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostIdOrderByOrderIndexAsc(Long postId);
    long countByPostId(Long postId);
    void deleteByPostId(Long postId);
}
