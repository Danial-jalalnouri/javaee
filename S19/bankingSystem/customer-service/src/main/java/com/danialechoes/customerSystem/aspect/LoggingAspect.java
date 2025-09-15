package com.danialechoes.customerSystem.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all controller methods
    @Pointcut("execution(* com.danialechoes.customerSystem.controller..*(..))")
    public void controllerMethods() {}

    // Pointcut for all service methods
    @Pointcut("execution(* com.danialechoes.customerSystem.service..*(..))")
    public void serviceMethods() {}

    // Pointcut for all repository methods
    @Pointcut("execution(* com.danialechoes.customerSystem.repository..*(..))")
    public void repositoryMethods() {}

    // Pointcut for all facade methods
    @Pointcut("execution(* com.danialechoes.customerSystem.facade..*(..))")
    public void facadeMethods() {}

    // Log method entry and exit for controller methods
    @Around("controllerMethods()")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.info("[CONTROLLER] Entering: {}.{} with arguments: {}",
                className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("[CONTROLLER] Successfully completed: {}.{} in {} ms",
                    className, methodName, (endTime - startTime));
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logger.error("[CONTROLLER] Failed: {}.{} in {} ms with error: {}",
                    className, methodName, (endTime - startTime), throwable.getMessage());
            throw throwable;
        }
    }

    // Log method entry and exit for service methods
    @Around("serviceMethods()")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.info("[SERVICE] Entering: {}.{} with arguments: {}",
                className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("[SERVICE] Successfully completed: {}.{} in {} ms",
                    className, methodName, (endTime - startTime));
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logger.error("[SERVICE] Failed: {}.{} in {} ms with error: {}",
                    className, methodName, (endTime - startTime), throwable.getMessage());
            throw throwable;
        }
    }

    // Log method entry and exit for repository methods
    @Around("repositoryMethods()")
    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.info("[REPOSITORY] Entering: {}.{} with arguments: {}",
                className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("[REPOSITORY] Successfully completed: {}.{} in {} ms",
                    className, methodName, (endTime - startTime));
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logger.error("[REPOSITORY] Failed: {}.{} in {} ms with error: {}",
                    className, methodName, (endTime - startTime), throwable.getMessage());
            throw throwable;
        }
    }

    // Log method entry and exit for facade methods
    @Around("facadeMethods()")
    public Object logFacadeMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        logger.info("[FACADE] Entering: {}.{} with arguments: {}",
                className, methodName, Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.info("[FACADE] Successfully completed: {}.{} in {} ms",
                    className, methodName, (endTime - startTime));
            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            logger.error("[FACADE] Failed: {}.{} in {} ms with error: {}",
                    className, methodName, (endTime - startTime), throwable.getMessage());
            throw throwable;
        }
    }
}
