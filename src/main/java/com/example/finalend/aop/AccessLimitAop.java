package com.example.finalend.aop;

import com.example.finalend.annotation.AccessLimit;
import com.example.finalend.exception.ApiException;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class AccessLimitAop {

    private final Map<String, RateLimiter> limiterMap = Maps.newConcurrentMap();

    @Around("@annotation(com.example.finalend.annotation.AccessLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AccessLimit annotation = signature.getMethod().getAnnotation(AccessLimit.class);
        if (annotation != null) {
            String key = annotation.key();
            RateLimiter rateLimiter;
            if (!limiterMap.containsKey(key)) {
                rateLimiter = RateLimiter.create(annotation.permitsPerSecond());
                limiterMap.put(key, rateLimiter);
            }
            rateLimiter = limiterMap.get(key);
            boolean acquire = rateLimiter.tryAcquire(annotation.timeout(), annotation.timeunit());
            if (!acquire) {
                throw new ApiException(annotation.msg());
            }
        }
        return joinPoint.proceed();
    }
}
