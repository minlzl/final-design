package com.example.finalend.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.example.finalend.annotation.HistoryLog;
import com.example.finalend.entity.History;
import com.example.finalend.mapper.HistoryMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
public class HistoryLogAspect {

    @Resource
    HistoryMapper historyMapper;

    @Pointcut("@annotation(com.example.finalend.annotation.HistoryLog)")
    public void pointCut() {

    }

    @Before("pointCut() && args(id)")
    public void saveHistoryLog(JoinPoint joinPoint, Long id) {
        long loginId = StpUtil.getLoginIdAsLong();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        HistoryLog annotation = method.getAnnotation(HistoryLog.class);
        History history = new History();
        if (annotation != null) {
            history.setType(annotation.type());
        }
        history.setContentId(id);
        history.setCreateTime(LocalDateTime.now());
        history.setUserId(loginId);
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        history.setIpAddr(getIpAddr(request));
        historyMapper.insert(history);
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
}
