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
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 게시글 단건 조회
     * @param postId 게시글ID
     * @return 게시글 상세 데이터
     */
    public Optional<PostDetailResponse> findPostById(Long postId, Long userId) {
        log.info("Finding post by id {} for user {}", postId, userId);
        QPost post = QPost.post;
        QUser user = QUser.user;
        QComment comment = QComment.comment;
        QPostImage postImage = QPostImage.postImage;
        QPostLike postLike = QPostLike.postLike;

        // 게시글 이미지
        List<String> imageUrls = queryFactory
                .select(postImage.url)
                .from(postImage)
                .where(postImage.postId.eq(postId))
                .orderBy(postImage.orderIndex.asc())
                .fetch();

        // 사용자 좋아요 여부
//        BooleanExpression isLiked = userId != null ? postLike.userId.eq(userId) : Expressions.asBoolean(false);
        BooleanExpression isLiked =
                userId == null ? Expressions.FALSE :
                        JPAExpressions.selectOne()
                                .from(postLike)
                                .where(postLike.postId.eq(post.id)
                                        .and(postLike.userId.eq(userId)))
                                .exists();

        // where 조건
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(post.id.eq(postId));

        PostDetailResponse response = queryFactory
                .select(new QPostDetailResponse(
                        post.id,
                        post.content,
                        Expressions.constant(""), // thumbnail
                        Expressions.constant(imageUrls),
                        user.nickname,
                        post.depth,
                        select(comment.count())
                                .from(comment)
                                .where(comment.parentId.eq(post.id)),
                        post.likeCount,
                        post.shareCount,
                        isLiked,
                        post.createdAt,
                        post.updatedAt
                ))
                .from(post)
                .leftJoin(user).on(post.createdBy.eq(user.id))
                .where(booleanBuilder)
                .fetchOne();

        return Optional.ofNullable(response);
    }


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
        }

        return query
                .offset(offset)
                .limit(condition.getSize())
                .orderBy(order)
                .fetch();
    }

    public List<FlatCommentDto> findCommentTreeByPostId(Long postId) {
        return null;
    }

    public Long countPosts() {
        QPost post = QPost.post;

        Long result = queryFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        return Optional.ofNullable(result).orElse(0L);
    }
}
