package com.loopin.loopinbackend.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FlatCommentDto {
    Long id;
    Long parentId;
    String authorNickname;
    String content;
    int depth;
    LocalDateTime createdAt;
    LocalDateTime updateAt;

    @Override
    public String toString() {
        return "FlatCommentDto{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", authorNickname=" + authorNickname +
                ", content='" + content + '\'' +
                ", depth=" + depth +
                ", createdAt=" + createdAt +
                '}';
    }
}
