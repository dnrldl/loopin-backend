package com.loopin.loopinbackend.domain.postlike.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class NotLikedException extends BaseException {
    public NotLikedException() {
        super(ErrorCode.POST_NOT_LIKED);
    }
}
