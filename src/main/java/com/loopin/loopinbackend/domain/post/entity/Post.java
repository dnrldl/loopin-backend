package com.loopin.loopinbackend.domain.post.entity;

import com.loopin.loopinbackend.global.entity.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false)
    private Integer depth ;

    @Column(nullable = false)
    private Long viewCount;
    @Column(nullable = false)
    private Long likeCount;
    @Column(nullable = false)
    private Long shareCount;
    @Column(nullable = false)
    private Long commentCount;
}
