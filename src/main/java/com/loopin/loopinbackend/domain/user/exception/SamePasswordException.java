package com.loopin.loopinbackend.domain.user.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class SamePasswordException extends BaseException {
    public SamePasswordException() {
        super(ErrorCode.PASSWORD_SAME_AS_OLD);
    }
}
