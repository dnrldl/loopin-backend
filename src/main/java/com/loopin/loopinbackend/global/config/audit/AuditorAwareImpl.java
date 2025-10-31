package com.loopin.loopinbackend.global.config.audit;

import com.loopin.loopinbackend.global.security.util.SecurityUtils;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        System.out.println("Auditor called");
        return Optional.ofNullable(SecurityUtils.getCurrentUser().getId());
    }
}
