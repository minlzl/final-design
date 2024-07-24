package com.example.finalend.annotation;

import org.redisson.api.RateType;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLimit {

    RateType mode() default RateType.PER_CLIENT;

    String[] keys() default {};

    long rate() default 100;

    long rateInterval() default 1;

    String showPromptMsg() default "服务器开小差了";
}
