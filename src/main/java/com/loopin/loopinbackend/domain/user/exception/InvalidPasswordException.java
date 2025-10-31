package com.loopin.loopinbackend.domain.user.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() { super(ErrorCode.INVALID_PASSWORD); }
}
