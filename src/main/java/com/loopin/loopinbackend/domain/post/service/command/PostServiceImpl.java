package com.loopin.loopinbackend.domain.post.service.command;

import com.loopin.loopinbackend.domain.post.dto.request.PostCreateRequest;
import com.loopin.loopinbackend.domain.post.dto.request.PostUpdateRequest;
import com.loopin.loopinbackend.domain.post.entity.Post;
import com.loopin.loopinbackend.domain.post.exception.PostNotFoundException;
import com.loopin.loopinbackend.domain.post.exception.PostNotOwnerException;
import com.loopin.loopinbackend.domain.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Long createPost(PostCreateRequest request, Long userId) {
        Post newPost = Post.builder()
                .content(request.getContent())
                .depth(0)
                .likeCount(0L)
                .shareCount(0L)
                .commentCount(0L)
                .viewCount(0L)
                .build();
        return postJpaRepository.save(newPost).getId();
    }

    @Override
    public void updatePost(PostUpdateRequest request, Long postId, Long userId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        if (!post.getCreatedBy().equals(userId)) throw new PostNotOwnerException();

        post.setContent(request.getContent());
        postJpaRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postJpaRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        if (!post.getCreatedBy().equals(userId)) throw new PostNotOwnerException();

        postJpaRepository.delete(post);
    }

    @Override
    public void createPosts() {
        ArrayList<Post> posts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            posts.add(
            Post.builder()
                    .content("Post " + i)
                    .depth(0)
                    .likeCount(0L)
                    .shareCount(0L)
                    .commentCount(0L)
                    .viewCount(0L)
                    .build());
        }

        postJpaRepository.saveAll(posts);
    }
}
