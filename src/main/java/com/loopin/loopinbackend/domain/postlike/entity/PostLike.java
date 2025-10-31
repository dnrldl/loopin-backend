package com.loopin.loopinbackend.domain.postlike.entity;

import com.loopin.loopinbackend.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(PostLikeId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostLike extends BaseTimeEntity {
    @Id
    private Long postId;
    @Id
    private Long userId;

    public static PostLike of(Long postId, Long userId) {
        return new PostLike(postId, userId);
    }
}
