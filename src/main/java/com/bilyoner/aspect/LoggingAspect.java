package com.bilyoner.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.bilyoner.controller.*.*(..))")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();

        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        String customerId = request.getHeader("X-Customer-Id");
        
        log.info("Request -> Method: [{}], URI: [{}], Customer: [{}], Arguments: {}",
                httpMethod, requestURI, customerId, Arrays.toString(joinPoint.getArgs()));

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        log.info("Response -> Method: [{}], URI: [{}], Customer: [{}], Response Time: {}ms",
                httpMethod, requestURI, customerId, (endTime - startTime));

        return result;
    }
} 