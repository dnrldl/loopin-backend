package com.loopin.loopinbackend.domain.post.service.command;

import com.loopin.loopinbackend.domain.post.dto.request.ImageRegisterRequest;

import java.util.List;

public interface PostImageService {
    // 업로드 완료 후 등록(스토리지에 이미 올라간 키/URL을 DB에 기록)
    void registerImages(Long postId, List<ImageRegisterRequest> images);

    // 전체 교체(기존 모두 삭제 + 새 목록 등록)
    void replaceAll(Long postId, List<ImageRegisterRequest> images);

    // 일부 삭제
    void deleteImages(Long postId, List<Long> imageIds);

    // 순서 변경 (클라이언트에서 정렬 드래그 후 전달)
    void reorder(Long postId, List<Long> orderedImageIds);

    // 대표 이미지 지정
    void setThumbnail(Long postId, Long imageId);
}
