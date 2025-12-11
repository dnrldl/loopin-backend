package com.loopin.loopinbackend.domain.comment.entity;

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
public class Comment extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorId;


    @Column(nullable = false)
    private Long parentId;

    private String content;

    private Integer depth;

    private Long likeCount;
    private Long shareCount;
    private Long commentCount;

    private Boolean isDeleted;
}
