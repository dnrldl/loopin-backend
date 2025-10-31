package com.loopin.loopinbackend.domain.media.api;

import com.loopin.loopinbackend.domain.media.domain.UploadPolicyRegistry;
import com.loopin.loopinbackend.domain.media.dto.PresignRequest;
import com.loopin.loopinbackend.global.response.ApiSuccessResponse;
import com.loopin.loopinbackend.global.storage.dto.PresignDtos;
import com.loopin.loopinbackend.global.storage.service.PresignService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Upload", description = "업로드 API")
@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final PresignService presignService;
    private final UploadPolicyRegistry policyRegistry;

    @PostMapping("/presign")
    public ResponseEntity<ApiSuccessResponse<List<PresignDtos.PresignUrlDetail>>> presign(@Valid @RequestBody PresignRequest req) {
        var policy = policyRegistry.get(req.usage()); // 용도별 정책 로드
        // 정책 기반 사전 검증(개수/확장자/최대크기 등)
        policy.validate(req);

        var files = req.files().stream()
                .map(f -> PresignDtos.FileSpec.builder()
                        .originalFileName(f.originalFileName())
                        .sizeBytes(f.sizeBytes())
                        .build())
                .collect(Collectors.toList());

        List<PresignDtos.PresignUrlDetail> presignUrlDetails = presignService.generatePresignedUrls(policy.resolvePathPrefix(req.pathPrefix()), files);
        return ResponseEntity.ok(ApiSuccessResponse.of(presignUrlDetails));
    }

}
