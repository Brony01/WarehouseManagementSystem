package com.java.warehousemanagementsystem.interceptor;


import com.java.warehousemanagementsystem.annotation.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor
{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        Method handlerMethod = ((HandlerMethod) handler).getMethod();
        RateLimit rateLimitByToken = handlerMethod.getAnnotation(RateLimit.class);
        if (rateLimitByToken == null) return true;

        String bearerToken = extractBearerToken(request);
        if (bearerToken == null || bearerToken.isEmpty()) throw new RuntimeException("Bearer token is required");

        String key = "rate_limit:" + handlerMethod.getDeclaringClass().getName() + ":" + handlerMethod.getName() + ":" + bearerToken;
        Long currentCount = redisTemplate.opsForValue().increment(key);
        if (currentCount == 1) redisTemplate.expire(key, rateLimitByToken.timeout(), TimeUnit.SECONDS);

        if (currentCount > rateLimitByToken.limit())
        {
            response.setContentType("application/xml;charset=UTF-8");
            String str = "请求次数过多！请稍后再试！";
            try {
                response.getWriter().write(str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }

        return true;
    }

    private String extractBearerToken(HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
