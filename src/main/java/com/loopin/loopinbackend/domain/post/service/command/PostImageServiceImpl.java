package com.loopin.loopinbackend.domain.post.service.command;

import com.loopin.loopinbackend.domain.post.dto.request.ImageRegisterRequest;
import com.loopin.loopinbackend.domain.post.entity.Post;
import com.loopin.loopinbackend.domain.post.entity.PostImage;
import com.loopin.loopinbackend.domain.post.repository.PostImageJpaRepository;
import com.loopin.loopinbackend.domain.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {
    private static final int MAX_IMAGES_PER_POST = 10;

    private final PostImageJpaRepository postImageJpaRepository;

    @Override
    public void registerImages(Long postId, List<ImageRegisterRequest> images) {
        List<PostImage> existing = postImageJpaRepository.findByPostIdOrderByOrderIndexAsc(postId);

        int total = existing.size() + images.size();
        if (total > MAX_IMAGES_PER_POST) {
            throw new IllegalArgumentException("이미지는 최대 " + MAX_IMAGES_PER_POST + "장까지 업로드할 수 있습니다.");
        }

        int start = existing.size();
        List<PostImage> toSave = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            var req = images.get(i);
            toSave.add(PostImage.builder()
                    .postId(postId)
                    .storageKey(req.storageKey())
                    .url(req.url())
                    .width(req.width())
                    .height(req.height())
                    .bytes(req.bytes())
                    .mimeType(req.mimeType())
                    .orderIndex(start + i)
                    .isThumbnail(start + i == 0 && existing.isEmpty()) // 첫 장이면 대표
                    .build());
        }
        postImageJpaRepository.saveAll(toSave);
    }

    @Override
    public void replaceAll(Long postId, List<ImageRegisterRequest> images) {

    }

    @Override
    public void deleteImages(Long postId, List<Long> imageIds) {
    }

    /**
     * 이미지 순서 변경
     * @param postId 변경할 게시글 ID
     * @param orderedImageIds 변경할 순서 ID 리스트
     */
    @Override
    public void reorder(Long postId, List<Long> orderedImageIds) {
        List<PostImage> images = postImageJpaRepository.findByPostIdOrderByOrderIndexAsc(postId);
        if (images.size() != orderedImageIds.size()) {
            throw new IllegalArgumentException("순서 목록이 현재 이미지 수와 일치하지 않습니다.");
        }

        Map<Long, PostImage> map = images.stream().collect(Collectors.toMap(PostImage::getId, it -> it));
        for (int i = 0; i < orderedImageIds.size(); i++) {
            PostImage pi = map.get(orderedImageIds.get(i));
            if (pi == null || !pi.getPostId().equals(postId)) {
                throw new IllegalArgumentException("잘못된 이미지 ID가 포함되어 있습니다.");
            }
            pi.setOrderIndex(i);
            pi.setIsThumbnail(i == 0);
        }
    }

    /**
     * 썸네일 이미지 지정
     * @param postId 지정할 게시글 ID
     * @param imageId 썸네일 적용할 이미지 ID
     */
    @Override
    public void setThumbnail(Long postId, Long imageId) {
        List<PostImage> images = postImageJpaRepository.findByPostIdOrderByOrderIndexAsc(postId);
        for (PostImage pi : images) {
            pi.setIsThumbnail(Objects.equals(pi.getId(), imageId));
        }
    }
}
