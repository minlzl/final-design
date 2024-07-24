package com.example.finalend.aop;

import com.example.finalend.annotation.RedisLimit;
import com.example.finalend.annotation.RedisLimits;
import com.example.finalend.exception.ApiException;
import com.example.finalend.utils.RedisLimitKeyUtil;
import io.lettuce.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class RedisLimitAspect {

    @Resource
    private RedisLimitKeyUtil redisLimitKeyUtil;

    @Resource
    private RedissonClient redissonClient;

    @Around(value = "@annotation(redisLimit)", argNames = "point, redisLimit")
    public Object around(ProceedingJoinPoint point, RedisLimit redisLimit) throws Throwable {
        isAllow(point, redisLimit);
        return point.proceed();
    }

    @Around(value = "@annotation(redisLimits)", argNames = "joinPoint, redisLimits")
    public Object around(ProceedingJoinPoint joinPoint, RedisLimits redisLimits) throws Throwable {
        RedisLimit[] limits = redisLimits.value();
        for (RedisLimit limit : limits) {
            isAllow(joinPoint, limit);
        }
        return joinPoint.proceed();
    }

    private void isAllow(ProceedingJoinPoint point, RedisLimit redisLimit) {
        String key = redisLimitKeyUtil.getKey(point, redisLimit);
        boolean flag = StringUtils.isNoneBlank(key);
        String uniqeKey = getUniqeKey((MethodSignature) point.getSignature());
        key = StringUtils.isNotBlank(key) ? uniqeKey + "." + key : uniqeKey;
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        limiter.trySetRate(redisLimit.mode(), redisLimit.rate(), redisLimit.rateInterval(), RateIntervalUnit.SECONDS);
        boolean tryAcquire = limiter.tryAcquire(1, redisLimit.rateInterval(), TimeUnit.SECONDS);
        if (!tryAcquire) {
            log.error("限流");
            throw new ApiException("限流");
        }
    }

    private String getUniqeKey(MethodSignature signature) {
        String format = String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod());
        return DigestUtils.md5DigestAsHex(format.getBytes(StandardCharsets.UTF_8));
    }
}
