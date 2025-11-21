package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class BlacklistTokenException extends AuthenticationException {
    public BlacklistTokenException() {
        super(ErrorCode.BLACKLISTED_TOKEN.getMessage());
    }
}
