package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class BlacklistTokenException extends BaseException {
    public BlacklistTokenException() {
        super(ErrorCode.BLACKLISTED_TOKEN);
    }
}
