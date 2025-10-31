package com.loopin.loopinbackend.domain.post.service.query;

import com.loopin.loopinbackend.domain.post.dto.FlatCommentDto;
import com.loopin.loopinbackend.domain.comment.dto.response.CommentResponse;
import com.loopin.loopinbackend.domain.post.dto.response.PostDetailResponse;
import com.loopin.loopinbackend.domain.post.qeury.PostSearchCond;
import com.loopin.loopinbackend.domain.post.repository.query.PostQueryRepository;
import com.loopin.loopinbackend.domain.postlike.repository.PostLikeRepository;
import com.loopin.loopinbackend.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostQueryServiceImpl implements PostQueryService {

    private final PostQueryRepository postQueryRepository;
    private final PostLikeRepository postLikeRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponse getPostInfo(Long postId, Long userId) {
        String redisKey = "post:" + postId + ":likes";
        Boolean isLiked = redisTemplate.opsForSet().isMember(redisKey, userId);

        PostDetailResponse response = postQueryRepository.findPostById(postId);

        if (Boolean.TRUE.equals(isLiked)) response.setIsLiked(true);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostDetailResponse> getPosts(PostSearchCond condition, Long userId) {

        int offset = condition.getPage() * condition.getSize();

        List<PostDetailResponse> contents = postQueryRepository.findPosts(offset, condition, userId);

//        for (PostInfoResponse content: contents) {
////            String redisKey = "post:" + content.getId() + ":likes";
////            Boolean isLiked = redisTemplate.opsForSet().isMember(redisKey, userId);
//            boolean isLiked = postLikeRepository.existsByPostIdAndUserId(content.getId(), userId);
//
//            if (isLiked) content.setIsLiked(true);
//        }

        Long count = postQueryRepository.countPosts();

        return PageResponse.of(contents, condition.getPage(), condition.getSize(), count, condition.getSortBy(), condition.getDirection());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentTree(Long postId) {
        List<FlatCommentDto> result = postQueryRepository.findCommentTreeByPostId(postId);
        return buildCommentTree(result, postId);
    }

    private List<CommentResponse> buildCommentTree(List<FlatCommentDto> flatList, Long postId) {
        Map<Long, CommentResponse> idToNode = new LinkedHashMap<>();
        List<CommentResponse> roots = new ArrayList<>();

        for (FlatCommentDto dto : flatList) {
            CommentResponse node = CommentResponse.from(dto);
            idToNode.put(node.getId(), node);

            if (Objects.equals(node.getParentId(), postId)) {
                roots.add(node);
            } else {
                CommentResponse parent = idToNode.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }
}
