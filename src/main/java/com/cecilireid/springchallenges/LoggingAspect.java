package com.cecilireid.springchallenges;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Value("${logging.aop.enabled:false}")
    private boolean loggingEnabled;

    @Around("execution(* com.cecilireid.springchallenges..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        if (!loggingEnabled) {
            return joinPoint.proceed();
        }

        String methodName = joinPoint.getSignature().toShortString();

        logger.info("-------------------------------------------");
        logger.info("{} << START", methodName);

        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            logger.info("{} >> END", methodName);
        }
    }
}
