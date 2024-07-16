package com.example.finalend.aop;

import com.example.finalend.annotation.RedisLimit;
import com.example.finalend.exception.ApiException;
import com.example.finalend.type.LimitType;
import com.example.finalend.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

@Aspect
@Component
@Slf4j
public class RedisLimitAspect {

    @Resource
    RedisTemplate redisTemplate;

    private static final DefaultRedisScript<Long> LIMIT_SCRIPT;

    static {
        LIMIT_SCRIPT = new DefaultRedisScript<>();
        LIMIT_SCRIPT.setLocation(new ClassPathResource("lua/limit.lua"));
        LIMIT_SCRIPT.setResultType(Long.class);
    }

    @Before("@annotation(redisLimit)")
    public void doBefore(JoinPoint point, RedisLimit redisLimit) throws Throwable {
        String key = redisLimit.key();
        int time = redisLimit.time();
        int count = redisLimit.count();
        String combineKey = getCombineKey(redisLimit, point);
        List<Object> keys = Collections.singletonList(combineKey);
        try {
            Long number = (Long) redisTemplate.execute(LIMIT_SCRIPT, keys, count, time);
            if (number==null || number.intValue() > count) {
                throw new ApiException("访问过于频繁，请稍候再试");
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), key);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

    public String getCombineKey(RedisLimit redisLimit, JoinPoint point) {
        StringBuilder sb = new StringBuilder(redisLimit.key());
        if (redisLimit.limitType() == LimitType.IP) {
            sb.append(IpUtils.getIpAddr(((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest())).append("-");
        }
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        sb.append(declaringClass.getName()).append("-").append(method.getName());
        return sb.toString();
    }

}
