package com.loopin.loopinbackend.domain.post.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class PostNotOwnerException extends BaseException {
    public PostNotOwnerException() {
        super(ErrorCode.POST_NOT_OWNER);
    }
}
