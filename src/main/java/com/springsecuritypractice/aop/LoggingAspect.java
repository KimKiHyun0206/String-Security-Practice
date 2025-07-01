package com.springsecuritypractice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * LogReturn 애노테이션이 붙은 메소드는 리턴값을 로깅한다
     * */
    @AfterReturning(pointcut = "@annotation(com.springsecuritypractice.aop.LogReturn)", returning = "result")
    public void logAnnotatedMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[RETURN] Method: {} | Returned Value: {}", methodName, result.toString());
    }
}