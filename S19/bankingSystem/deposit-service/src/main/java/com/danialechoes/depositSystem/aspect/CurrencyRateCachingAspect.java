package com.danialechoes.depositSystem.aspect;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Aspect
@Component
public class CurrencyRateCachingAspect {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRateCachingAspect.class);

    private final ConcurrentMap<String, CacheEntry> exchangeRateCache = new ConcurrentHashMap<>();

    @Value("${cache.exchange-rate.ttl-minutes:10}")
    private long cacheTtlMinutes;

    @Value("${cache.exchange-rate.cleanup-interval:50}")
    private long cleanupInterval;

    private volatile long cacheHits = 0;
    private volatile long cacheMisses = 0;
    private volatile long totalRequests = 0;

    @Pointcut("execution(* com.danialechoes.depositSystem.service.CurrencyService.getExchangeRate(..))")
    public void currencyRateServiceMethod() {}

    @Around("currencyRateServiceMethod()")
    public Object cacheExchangeRate(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        totalRequests++;

        if(args.length >= 2 &&
                args[0] != null &&
                args[1] != null) {
            String baseCurrency = args[0].toString();
            String targetCurrency = args[1].toString();
            String cacheKey = generateCacheKey(baseCurrency, targetCurrency);

            CacheEntry cachedEntry = exchangeRateCache.get(cacheKey);
            if (cachedEntry != null && !cachedEntry.isExpired()) {
                cacheHits++;
                logger.info("[CurrencyRateCachingAspect] Cache hit for {} to {}", baseCurrency, targetCurrency);
                return cachedEntry.getValue();
            }

            cacheMisses++;
            double hitRate = (double) cacheHits / totalRequests * 100;

            logger.info("[CurrencyRateCachingAspect] Cache miss for {} to {}. Hit rate: {:.2f}%",
                    baseCurrency, targetCurrency, hitRate);

            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed(args);
            long apiCallDuration = System.currentTimeMillis() - startTime;

            if(result != null && result instanceof Double) {
                long ttlMs = cacheTtlMinutes * 60 * 1000;
                long expirationTime = System.currentTimeMillis() + ttlMs;
                exchangeRateCache.put(cacheKey, new CacheEntry(result, expirationTime));

                logger.info("[CurrencyRateCachingAspect] Cached exchange rate for {} to {}: {} (API call took {} ms)",
                        baseCurrency, targetCurrency, result, apiCallDuration);

                if(totalRequests % cleanupInterval == 0) {
                    cleanExpiredEntries();
                }
            }

        }

        return joinPoint.proceed(args);
    }

    private void cleanExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        int beforeSize = exchangeRateCache.size();

        exchangeRateCache.entrySet().removeIf(entry -> entry.getValue().isExpired());

        int afterSize = exchangeRateCache.size();
        int removedCount = beforeSize - afterSize;

        if (removedCount > 0) {
            logger.info("[CurrencyRateCachingAspect] Cleaned up {} expired cache entries. Current cache size: {}",
                    removedCount, afterSize);
        }
    }

    private String generateCacheKey(String baseCurrency, String targetCurrency) {
        return String.format("exchange_rate:%s:%s"
                , baseCurrency, targetCurrency);
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
