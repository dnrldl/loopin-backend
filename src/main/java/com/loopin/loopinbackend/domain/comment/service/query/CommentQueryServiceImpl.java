package com.loopin.loopinbackend.domain.comment.service.query;

import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;
import com.loopin.loopinbackend.domain.comment.repository.query.CommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService{

    private final CommentQueryRepository commentQueryRepository;

    public List<CommentResponse> getComments(Long postId, Long userId) {

        return commentQueryRepository.findComments(postId);
    }

}
