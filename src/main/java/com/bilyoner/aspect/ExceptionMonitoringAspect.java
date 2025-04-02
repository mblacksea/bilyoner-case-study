package com.bilyoner.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ExceptionMonitoringAspect {

    @AfterThrowing(pointcut = "execution(* com.bilyoner..*.*(..))", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        if (!isHandledException(exception)) {
            log.error("Exception in {} with args = {}", 
                    joinPoint.getSignature().toShortString(),
                    Arrays.toString(joinPoint.getArgs()),
                    exception);
        }
    }

    private boolean isHandledException(Throwable exception) {
        return exception instanceof com.bilyoner.exception.OddsChangedException ||
               exception instanceof IllegalStateException ||
               exception instanceof IllegalArgumentException;
    }
} 