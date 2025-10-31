package com.loopin.loopinbackend.domain.media.domain;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UploadPolicyRegistry {
    private final Map<String, UploadPolicy> policies;

    public UploadPolicyRegistry() {
        this.policies = Map.of(
                "POST_IMAGE", UploadPolicy.builder()
                        .allowedExt(new String[]{".jpg",".jpeg",".png",".gif",".webp",".avif"})
                        .maxFiles(10)
                        .maxBytesPerFile(10 * 1024 * 1024L)
                        .fixedPrefix("images/post/") // 년/월은 컨트롤러에서 붙이거나 프론트에서 넘김
                        .build(),
                "USER_PROFILE_IMAGE", UploadPolicy.builder()
                        .allowedExt(new String[]{".jpg",".jpeg",".png",".webp"})
                        .maxFiles(1)
                        .maxBytesPerFile(3 * 1024 * 1024L)
                        .fixedPrefix("images/profile/")
                        .build()
        );
    }

    public UploadPolicy get(String usage) {
        var p = policies.get(usage);
        if (p == null) throw new IllegalArgumentException("알 수 없는 usage: " + usage);
        return p;
    }
}
