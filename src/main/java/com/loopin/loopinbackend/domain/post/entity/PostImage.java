package com.loopin.loopinbackend.domain.post.entity;

import com.loopin.loopinbackend.global.entity.BaseAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false, length = 255)
    private String storageKey;

    @Column(nullable = false, length = 1024)
    private String url;

    private Integer width;
    private Integer height;

    private Long bytes;

    @Column(length = 100)
    private String mimeType;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Boolean isThumbnail;
}
