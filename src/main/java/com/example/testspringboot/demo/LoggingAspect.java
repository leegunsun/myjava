package com.example.testspringboot.demo;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut: 모든 서비스 클래스의 메서드
    @Pointcut("execution(* com.example.testspringboot.demo.*.*(..))")
    public void serviceMethods() {}

    // Before Advice: 메서드 실행 전에 로깅
    @Before("serviceMethods()")
    public void logBefore() {
        logger.info("메서드 실행 전: 로그 기록 중...");
    }

    // After Advice: 메서드 실행 후에 로깅
    @After("serviceMethods()")
    public void logAfter() {
        logger.info("메서드 실행 후: 로그 기록 완료.");
    }

    // Around Advice: 메서드 실행 전후에 로깅
    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Around - 실행 전: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();  // 메서드 실행
        logger.info("Around - 실행 후: " + joinPoint.getSignature().getName());
        return result;
    }
}