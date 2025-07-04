package com.example._Board.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LoggingConfig {

    private static final Logger logger = Logger.getLogger(LoggingConfig.class.getName());
    private final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    @Pointcut("execution(* com.example._Board..*.*(..))")
    public void logging() {}

    @Before("logging()")
    public void logStart(JoinPoint joinPoint) {
        startTimeThreadLocal.set(System.currentTimeMillis());
        logger.info("시작 메서드: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "logging()", returning = "result")
    public void logEnd(JoinPoint joinPoint, Object result) {
        long endTime = System.currentTimeMillis();
        Long startTime = startTimeThreadLocal.get();
        if (startTime != null) {
            long duration = endTime - startTime;
            logger.info("위치 : " + joinPoint.getSignature() + " / 걸린 시간 : " + duration + " ms");
            startTimeThreadLocal.remove();
        }
    }
}
