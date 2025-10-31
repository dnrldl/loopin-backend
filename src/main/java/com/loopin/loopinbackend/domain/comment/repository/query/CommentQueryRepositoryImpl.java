package com.loopin.loopinbackend.domain.comment.repository.query;

import com.loopin.loopinbackend.domain.comment.dto.response.QCommentResponse;
import com.loopin.loopinbackend.domain.comment.entity.QComment;
import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;
import com.loopin.loopinbackend.domain.user.entity.QUser;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.NullExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<CommentResponse> findComments(Long postId) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        return queryFactory.select(new QCommentResponse(
                        comment.id,
                        comment.parentId,
                        comment.content,
                        user.nickname,
                        comment.depth,
                        comment.createdAt,
                        comment.updatedAt,
                    ConstantImpl.create(Collections.emptyList())
                ))
                .from(comment)
                .join(user).on(comment.createdBy.eq(user.id))
                .where(comment.parentId.eq(postId))
                .orderBy(comment.createdAt.desc())
                .fetch();
    }

}
