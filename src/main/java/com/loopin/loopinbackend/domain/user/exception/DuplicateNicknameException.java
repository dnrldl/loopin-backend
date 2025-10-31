package com.loopin.loopinbackend.domain.user.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class DuplicateNicknameException extends BaseException {
    public DuplicateNicknameException() { super(ErrorCode.DUPLICATE_NICKNAME); }
}
