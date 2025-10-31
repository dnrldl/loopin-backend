package com.loopin.loopinbackend.domain.comment.service.command;

import com.loopin.loopinbackend.domain.comment.dto.request.CommentCreateRequest;
import com.loopin.loopinbackend.domain.comment.entity.Comment;
import com.loopin.loopinbackend.domain.comment.repository.CommentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentJpaRepository commentJpaRepository;
    @Override
    public Long createComment(CommentCreateRequest request, Long postId, Long userId) {

        Comment comment = Comment.builder()
                .authorId(userId)
                .parentId(postId)
                .content(request.getContent())
                .depth(0)
                .likeCount(0L)
                .shareCount(0L)
                .commentCount(0L)
                .build();

        return commentJpaRepository.save(comment).getId();
    }
}
