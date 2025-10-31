package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class InvalidRefreshTokenException extends BaseException {
    public InvalidRefreshTokenException() { super(ErrorCode.INVALID_REFRESH_TOKEN); }
}
