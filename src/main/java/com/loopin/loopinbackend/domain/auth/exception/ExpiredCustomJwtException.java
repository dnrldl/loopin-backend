package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class ExpiredCustomJwtException extends BaseException {
    public ExpiredCustomJwtException() { super(ErrorCode.EXPIRED_JWT); }
}
