package com.loopin.loopinbackend.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseAuditingEntity extends BaseTimeEntity {
    @CreatedBy
    private Long createdBy; // 엔티티 처음 생성시

    @LastModifiedBy
    private Long updatedBy; // 엔티티 처음 생성 and 변경시
}
