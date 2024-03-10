package com.example.finalend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    String key() default "";
    int permitsPerSecond () default 1;
    int timeout() default 100;
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;
    String msg() default "系统繁忙，请稍后再试";
}
