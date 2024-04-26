package com.java.warehousemanagementsystem.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@Component
public class CacheLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheLoggingAspect.class);

    @Around("@annotation(org.springframework.cache.annotation.Cacheable)")
    public Object cacheableAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();
        String key = args.length > 0 ? args[0].toString() : "No Key";

        logger.info("Attempting to access cache for method: {}, key: {}", methodName, key);
        Object result = null;
        try {
            result = joinPoint.proceed();
            if (result != null) {
                logger.info("Cache hit for method: {}, key: {}", methodName, key);
            } else {
                logger.info("Cache miss for method: {}, key: {}", methodName, key);
            }
        } catch (Throwable t) {
            logger.error("Error accessing cache for method: {}, key: {}", methodName, key);
            throw t;
        }
        return result;
    }

    @Around("@annotation(org.springframework.cache.annotation.CachePut) || @annotation(org.springframework.cache.annotation.CacheEvict)")
    public Object cachePutEvictAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();
        String key = args.length > 0 ? args[0].toString() : "No Key";
        Object result = joinPoint.proceed();

        if (signature.getMethod().isAnnotationPresent(org.springframework.cache.annotation.CachePut.class)) {
            logger.info("Cache updated for method: {}, key: {}", methodName, key);
        } else if (signature.getMethod().isAnnotationPresent(org.springframework.cache.annotation.CacheEvict.class)) {
            logger.info("Cache evicted for method: {}, key: {}", methodName, key);
        }

        return result;
    }
}

