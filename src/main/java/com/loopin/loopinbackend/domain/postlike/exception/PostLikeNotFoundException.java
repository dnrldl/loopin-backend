package com.loopin.loopinbackend.domain.postlike.exception;

import com.loopin.loopinbackend.global.error.BaseException;
import com.loopin.loopinbackend.global.error.ErrorCode;

public class PostLikeNotFoundException extends BaseException {
    public PostLikeNotFoundException() {
        super(ErrorCode.POST_LIKE_NOT_FOUND);
    }
}
