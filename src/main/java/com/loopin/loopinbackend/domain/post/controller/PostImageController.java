package com.loopin.loopinbackend.domain.post.controller;

import com.loopin.loopinbackend.domain.post.dto.request.ImageRegisterRequest;
import com.loopin.loopinbackend.domain.post.service.command.PostImageService;
import com.loopin.loopinbackend.global.response.ApiSuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/images")
@RequiredArgsConstructor
public class PostImageController {
    private final PostImageService postImageService;

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse<String>> register(@PathVariable Long postId, @RequestBody @Valid List<ImageRegisterRequest> req) {
        postImageService.registerImages(postId, req);
        return ResponseEntity.ok(ApiSuccessResponse.of("이미지 등록 성공"));
    }

    @PutMapping("/replace")
    public void replace(@PathVariable Long postId, @RequestBody @Valid List<ImageRegisterRequest> req) {
        postImageService.replaceAll(postId, req);
    }

    @PatchMapping("/reorder")
    public void reorder(@PathVariable Long postId) {
//        postImageService.reorder(postId, req.imageIds());
    }

    @PatchMapping("/{imageId}/thumbnail")
    public void setThumb(@PathVariable Long postId, @PathVariable Long imageId) {
        postImageService.setThumbnail(postId, imageId);
    }

    @DeleteMapping("/{imageId}")
    public void delete(@PathVariable Long postId, @PathVariable Long imageId) {
        postImageService.deleteImages(postId, List.of(imageId));
    }
}
