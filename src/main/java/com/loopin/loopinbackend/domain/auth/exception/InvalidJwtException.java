package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {
    public InvalidJwtException() { super(ErrorCode.INVALID_JWT.getMessage()); }
}
