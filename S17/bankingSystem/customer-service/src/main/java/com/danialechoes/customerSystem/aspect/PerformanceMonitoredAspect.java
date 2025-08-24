package com.danialechoes.customerSystem.aspect;

import com.danialechoes.customerSystem.annotation.PerformanceMonitored;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceMonitoredAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitoredAspect.class);

    @Around("@annotation(performanceMonitored)")
    public Object monitorPerformance(org.aspectj.lang.ProceedingJoinPoint joinPoint, PerformanceMonitored performanceMonitored) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String category = performanceMonitored.category();
        long threshold = performanceMonitored.threshold();

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            if (executionTime > threshold && performanceMonitored.logSlowOperations()) {
                logger.warn("[PERFORMANCE] [{}] Slow operation detected: {}. Duration: {} ms. Category: {}",
                        className, methodName, executionTime, category);
            } else {
                logger.info("[PERFORMANCE] [{}] Operation completed: {}. Duration: {} ms. Category: {}",
                        className, methodName, executionTime, category);
            }
            return result;
        } catch (Throwable throwable) {
            logger.error("[PERFORMANCE] [{}] Exception in method {}: {}", className, methodName, throwable.getMessage());
            throw throwable;
        }
    }
}
