package com.danialechoes.customerSystem.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceMonitored {
    long threshold() default 1000;

    String category() default "GENERAL";

    boolean logSlowOperations() default true;

}
