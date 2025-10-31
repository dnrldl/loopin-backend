package com.loopin.loopinbackend.domain.auth.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class InvalidLoginException extends BaseException {
    public InvalidLoginException() { super(ErrorCode.INVALID_LOGIN); }
}
