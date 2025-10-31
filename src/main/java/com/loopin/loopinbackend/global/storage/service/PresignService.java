package com.loopin.loopinbackend.global.storage.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.loopin.loopinbackend.global.storage.dto.PresignDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PresignService {

    private final AmazonS3 amazonS3;

    // ===== 환경 설정 =====
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloudfront.domain}")
    private String cloudFrontDomain;

    // 정책(필요에 맞게 조정)
    // presigned 만료: 5분
    private static final long EXPIRE_MILLIS = 5 * 60 * 1000;
    private static final int MAX_FILES = 10;
    private static final long MAX_BYTES_PER_FILE = 10L * 1024 * 1024; // 10MB
    private static final Set<String> ALLOWED_EXT = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".avif", ".svg");
    private static final Map<String, String> EXT_TO_MIME = Map.ofEntries(
            Map.entry(".jpg", "image/jpeg"),
            Map.entry(".jpeg", "image/jpeg"),
            Map.entry(".png", "image/png"),
            Map.entry(".gif", "image/gif"),
            Map.entry(".webp", "image/webp"),
            Map.entry(".avif", "image/avif"),
            Map.entry(".svg", "image/svg+xml")
    );


    /**
     * 다중 파일 presigned URL 발급
     * @param pathPrefix example: "images/post/2025/08/"
     * @param files      업로드할 파일들
     */
    public List<PresignDtos.PresignUrlDetail> generatePresignedUrls(String pathPrefix, List<PresignDtos.FileSpec> files) {
        // 1) 기본 검증
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
        if (files.size() > MAX_FILES) {
            throw new IllegalArgumentException("파일은 최대 " + MAX_FILES + "개까지 업로드할 수 있습니다.");
        }

        // 2) 경로 정규화
        String prefix = normalizePrefix(pathPrefix);
        System.out.println("prefix = " + prefix);

        // 3) 파일별 presign
        List<PresignDtos.PresignUrlDetail> result = new ArrayList<>(files.size());
        for (PresignDtos.FileSpec f : files) {
            String original = requireNonBlank(f.originalFileName(), "원본 파일명이 비어 있습니다.");
            System.out.println("original = " + original);

            // 3-1) 확장자/용량 검증
            String ext = extractExt(original);
            System.out.println("ext = " + ext);
            validateExtension(ext);
            if (f.sizeBytes() != null && f.sizeBytes() > MAX_BYTES_PER_FILE) {
                throw new IllegalArgumentException("파일(" + original + ")이 최대 용량(" + readableSize(MAX_BYTES_PER_FILE) + ")을 초과했습니다.");
            }

            // 3-2) 서버가 강제할 Content-Type 결정
            String contentType = EXT_TO_MIME.getOrDefault(ext, "application/octet-stream");
            System.out.println("contentType = " + contentType);

            // 3-3) 새 파일명 생성
            String newFileName = UUID.randomUUID() + ext;
            String key = prefix + newFileName;

            System.out.println("key = " + key);
            System.out.println("newFileName = " + newFileName);

            // 3-4) 만료 시간
            Date expiration = Date.from(Instant.ofEpochMilli(System.currentTimeMillis() + EXPIRE_MILLIS));
            System.out.println("expiration = " + expiration);

            // 3-5) presigned 생성 (Content-Type 고정)
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, key)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(expiration)
                    .withContentType(contentType);

            // 메타데이터 헤더는 서명 파라미터로 강제하긴 어렵지만
            // 업로드 시 클라이언트가 반드시 아래 헤더를 넣도록 규약화
            // 필요 시 key에 원본명 해시 포함도 고려.
            URL presignedUrl = amazonS3.generatePresignedUrl(req);

            System.out.println("presignedUrl = " + presignedUrl);

            String fileUrl = cloudFrontDomain + "/" + key;

            System.out.println("fileUrl = " + fileUrl);

            result.add(PresignDtos.PresignUrlDetail.builder()
                    .key(key)
                    .presignedUrl(presignedUrl.toString())
                    .fileUrl(fileUrl)
                    .contentType(contentType)
                    .requiredHeaderOriginalName(original) // x-amz-meta-original-filename 로 업로드 때 세팅
                    .build());
        }

        return result;
    }

    // ===== Helper =====

    /**
     * 파일 경로 정제
     * @param pathPrefix 파일 경로
     * @return uploads/user/
     */
    private static String normalizePrefix(String pathPrefix) {
        String p = Optional.ofNullable(pathPrefix).orElse("");
        p = p.replace("\\", "/");
        if (p.startsWith("/")) p = p.substring(1);
        if (!p.isEmpty() && !p.endsWith("/")) p = p + "/";
        // 경로 주입 방지(상대 경로 조작)
        if (p.contains("..")) throw new IllegalArgumentException("잘못된 경로입니다.");
        return p;
    }

    /**
     * 확장자 추출
     * @param original 파일명
     * @return 확장자
     */
    private static String extractExt(String original) {
        String name = original.trim();
        int idx = name.lastIndexOf('.');
        if (idx < 0) throw new IllegalArgumentException("확장자가 없습니다: " + original);
        return name.substring(idx).toLowerCase(Locale.ROOT);
    }

    /**
     * 확장자 검증
     * @param ext 확장자
     */
    private static void validateExtension(String ext) {
        if (!ALLOWED_EXT.contains(ext)) {
            throw new IllegalArgumentException("허용되지 않는 확장자입니다: " + ext);
        }
    }

    private static String requireNonBlank(String v, String msg) {
        if (!StringUtils.hasText(v)) throw new IllegalArgumentException(msg);
        return v;
    }

    private static String readableSize(long bytes) {
        double b = bytes;
        String[] u = {"B","KB","MB","GB"};
        int i = 0;
        while (b >= 1024 && i < u.length - 1) {
            b /= 1024; i++;
        }
        return String.format(Locale.ROOT, "%.1f%s", b, u[i]);
    }
}
