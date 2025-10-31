package com.loopin.loopinbackend.domain.user.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() { super(ErrorCode.USER_NOT_FOUND); }
}
