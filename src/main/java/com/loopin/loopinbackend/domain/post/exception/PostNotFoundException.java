package com.loopin.loopinbackend.domain.post.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class PostNotFoundException extends BaseException {
    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }
}
