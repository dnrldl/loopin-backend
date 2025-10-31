package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class EmptyTokenException extends BaseException {
    public EmptyTokenException() { super(ErrorCode.EMPTY_JWT); }
}
