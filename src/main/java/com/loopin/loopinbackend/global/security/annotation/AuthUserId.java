package com.loopin.loopinbackend.global.security.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUserId {

    long defaultValue() default 0L;

    /**
     * true: 인증 필요
     * false: 비로그인 허용
     */
    boolean required() default true;
}
