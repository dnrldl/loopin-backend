package com.loopin.loopinbackend.domain.post.repository.query;

import com.loopin.loopinbackend.domain.comment.entity.QComment;
import com.loopin.loopinbackend.domain.post.dto.FlatCommentDto;
import com.loopin.loopinbackend.domain.post.dto.response.PostDetailResponse;
import com.loopin.loopinbackend.domain.post.dto.response.QPostDetailResponse;
import com.loopin.loopinbackend.domain.post.entity.QPost;
import com.loopin.loopinbackend.domain.post.entity.QPostImage;
import com.loopin.loopinbackend.domain.post.exception.PostNotFoundException;
import com.loopin.loopinbackend.domain.post.qeury.PostSearchCond;
import com.loopin.loopinbackend.domain.postlike.entity.QPostLike;
import com.loopin.loopinbackend.domain.user.entity.QUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public PostDetailResponse findPostById(Long postId) {
        QPost post = QPost.post;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QPostImage postImage = QPostImage.postImage;

        // 1) 이미지 URL 리스트 (orderIndex 순)
        List<String> imageUrls = queryFactory
                .select(postImage.url)
                .from(postImage)
                .where(postImage.postId.eq(postId))
                .orderBy(postImage.orderIndex.asc())
                .fetch();

        // 2) 단건 상세 조회 (DTO 생성자 순서와 EXACT 매칭)
        PostDetailResponse response = queryFactory
                .select(new QPostDetailResponse(
                        post.id,
                        post.content,
                        Expressions.constant(""),
                        Expressions.constant(imageUrls),     // ③ imageUrls (List<String>)
                        user.nickname,                       // ④ authorNickname
                        post.depth,                          // ⑤ depth
                        // ⑥ commentCount
                        select(comment.count())
                                .from(comment)
                                .where(comment.parentId.eq(post.id)),   // 보통 postId 기준
                        post.likeCount,                      // ⑦ likeCount
                        post.shareCount,                     // ⑧ shareCount
                        Expressions.constant(false),         // ⑨ isLiked (Redis로 별도 세팅)
                        post.createdAt,                      // ⑩ createdAt
                        post.updatedAt                       // ⑪ updatedAt
                ))
                .from(post)
                .leftJoin(user).on(post.createdBy.eq(user.id))
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(response)
                .orElseThrow(PostNotFoundException::new);
    }


    @Override
    public List<PostDetailResponse> findPosts(int offset, PostSearchCond condition, Long userId) {
        QPost post = QPost.post;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QPostLike postLike = QPostLike.postLike;
        QPostImage postImage = QPostImage.postImage;

        String sortBy = Optional.ofNullable(condition.getSortBy()).orElse("createdAt");
        String direction = Optional.ofNullable(condition.getDirection()).orElse("desc");

        OrderSpecifier<?> order = switch (sortBy) {
            case "likeCount" -> "asc".equalsIgnoreCase(direction) ? post.likeCount.asc() : post.likeCount.desc();
            case "commentCount" -> "asc".equalsIgnoreCase(direction) ? post.commentCount.asc() : post.commentCount.desc();
            case "shareCount" -> "asc".equalsIgnoreCase(direction) ? post.shareCount.asc() : post.shareCount.desc();
            default -> "asc".equalsIgnoreCase(direction) ? post.createdAt.asc() : post.createdAt.desc();
        };

        var thumbnailExpr = select(postImage.url)
                .from(postImage)
                .where(
                        postImage.postId.eq(post.id)      // post 별칭이 외부쿼리에 있으므로 OK
                                .and(postImage.isThumbnail.isTrue())
                );

        var query = queryFactory
                .select(new QPostDetailResponse(
                        post.id,
                        post.content,
                        thumbnailExpr,
                        Expressions.constant(List.of()),
                        user.nickname,
                        post.depth,
                        select(comment.count())
                                .from(comment)
                                .where(comment.parentId.eq(post.id)),
                        post.likeCount,
                        post.shareCount,
                        Expressions.constant(false),
                        post.createdAt,
                        post.updatedAt
                ))
                .from(post)
                .leftJoin(user).on(post.createdBy.eq(user.id));

        if (userId != null) {
            query.leftJoin(postLike)
                    .on(postLike.postId.eq(post.id)
                            .and(postLike.userId.eq(userId)));
            // 필요시 isLiked 계산 실시간 반영:
            // .select(...) 내에
            // new CaseBuilder().when(postLike.id.isNotNull()).then(true).otherwise(false)
        }

        return query
                .offset(offset)
                .limit(condition.getSize())
                .orderBy(order)
                .fetch();
    }

    @Override
    public List<FlatCommentDto> findCommentTreeByPostId(Long postId) {
        return null;
    }

    @Override
    public Long countPosts() {
        QPost post = QPost.post;

        Long result = queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        return Optional.ofNullable(result).orElse(0L);
    }
}
