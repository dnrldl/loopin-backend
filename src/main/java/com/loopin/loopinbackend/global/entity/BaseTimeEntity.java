package com.loopin.loopinbackend.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @Column(updatable = false)
    private LocalDateTime createdAt;  // 엔티티 처음 생성시
    private LocalDateTime updatedAt;  // 엔티티 처음 생성 and 변경시

    @PrePersist
    public void PrePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void PreUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
