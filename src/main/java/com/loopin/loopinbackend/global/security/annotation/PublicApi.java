package com.loopin.loopinbackend.global.security.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface PublicApi {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default {};
}
