package com.java.warehousemanagementsystem.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.util.UUID;

@Aspect
@Component
public class LoggingAspect
{

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable
    {
        // 记录入参
        ObjectMapper objectMapper = new ObjectMapper();
        String args = objectMapper.writeValueAsString(joinPoint.getArgs());
        LoggerFactory.getLogger(joinPoint.getTarget().getClass()).info(" Request: {}", args);

        // 调用原方法
        Object result = joinPoint.proceed();

        // 记录出参
        String response = objectMapper.writeValueAsString(result);
        LoggerFactory.getLogger(joinPoint.getTarget().getClass()).info(" Response: {}", response);

        // 清除 MDC 中的 logID
        MDC.remove("logId");

        return result;
    }
}
