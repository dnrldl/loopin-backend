package com.loopin.loopinbackend.domain.postlike.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class AlreadyLikedException extends BaseException {

    public AlreadyLikedException() {
        super(ErrorCode.POST_ALREADY_LIKED);
    }
}
