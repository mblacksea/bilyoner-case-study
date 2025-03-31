package com.bilyoner.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Value("${app.execution.time-threshold}")
    private long executionTimeThreshold;

    @Around("@within(org.springframework.stereotype.Service)")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if (executionTime > executionTimeThreshold) {
            log.warn("Long execution detected -> Method: [{}], Time: {}ms",
                    joinPoint.getSignature().toShortString(), executionTime);
        }

        return result;
    }
} 