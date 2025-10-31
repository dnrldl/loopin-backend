package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class UnauthorizedAccessException extends BaseException {
    public UnauthorizedAccessException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
