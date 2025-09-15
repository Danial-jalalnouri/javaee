package com.danialechoes.customerSystem.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
public class CachingAspect {

    private static final Logger logger = LoggerFactory.getLogger(CachingAspect.class);

    private final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private static final long CACHE_TTL_MS = 300000; // 5 minutes

    @Pointcut("execution(* com.danialechoes.customerSystem.service.CustomerService.getCustomerById(..))")
    public void getCustomerByIdPointcut() {}

    @Pointcut("execution(* com.danialechoes.customerSystem.service.CustomerService.getAllCustomers(..))")
    public void getAllCustomersPointcut() {}

    // Cache customer retrieval by ID
    @Around("getCustomerByIdPointcut()")
    public Object cacheGetCustomerById(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if(args.length > 0 && args[0] != null){
            String cacheKey = "customer:" + args[0];
            CacheEntry cachedEntry = cache.get(cacheKey);
            if (cachedEntry != null && !cachedEntry.isExpired()) {
                logger.info("Cache hit for {}", cacheKey);
                return cachedEntry.getValue();
            }
            logger.info("Cache miss for {}", cacheKey);
            Object result = joinPoint.proceed();

            if(result != null) {
                cache.put(cacheKey, new CacheEntry(result, System.currentTimeMillis() + CACHE_TTL_MS));
                logger.info("Cached result for {}", cacheKey);
            }
            return result;
        }
        return joinPoint.proceed();
    }

    // Cache all customers retrieval
    //@Around("getAllCustomersPointcut()")
    public Object cacheGetAllCustomers(ProceedingJoinPoint joinPoint) throws Throwable {
        String cacheKey = "customer:all";
        CacheEntry cachedEntry = cache.get(cacheKey);
        if (cachedEntry != null && !cachedEntry.isExpired()) {
            logger.info("Cache hit for {}", cacheKey);
            return cachedEntry.getValue();
        }
        logger.info("Cache miss for {}", cacheKey);
        Object result = joinPoint.proceed();

        if(result != null) {
            cache.put(cacheKey, new CacheEntry(result, System.currentTimeMillis() + CACHE_TTL_MS));
            logger.info("Cached result for {}", cacheKey);
        }
        return result;
    }

    private static class CacheEntry {
        private final Object value;
        private final long expirationTime;

        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
