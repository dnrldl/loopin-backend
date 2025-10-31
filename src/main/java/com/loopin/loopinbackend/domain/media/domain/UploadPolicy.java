package com.loopin.loopinbackend.domain.media.domain;

import com.loopin.loopinbackend.domain.media.dto.PresignRequest;
import lombok.Builder;

import java.util.Arrays;
import java.util.Locale;

@Builder
public class UploadPolicy {
    String[] allowedExt;
    int maxFiles;
    long maxBytesPerFile;
    String fixedPrefix; // 있으면 pathPrefix 무시하고 강제

    public void validate(PresignRequest req) {
        if (req.files().size() > maxFiles) {
            throw new IllegalArgumentException("파일은 최대 " + maxFiles + "개까지 가능합니다.");
        }
        for (var f : req.files()) {
            var name = f.originalFileName();
            var idx = name.lastIndexOf('.');
            if (idx < 0) throw new IllegalArgumentException("확장자가 없습니다: " + name);
            var ext = name.substring(idx).toLowerCase(Locale.ROOT);
            if (Arrays.stream(allowedExt).noneMatch(e -> e.equals(ext))) {
                throw new IllegalArgumentException("허용되지 않는 확장자: " + ext);
            }
            if (f.sizeBytes() != null && f.sizeBytes() > maxBytesPerFile) {
                throw new IllegalArgumentException("파일(" + name + ") 용량 초과");
            }
        }
    }

    public String resolvePathPrefix(String requested) {
        if (fixedPrefix != null && !fixedPrefix.isBlank()) return fixedPrefix;
        return requested == null ? "" : requested;
    }
}