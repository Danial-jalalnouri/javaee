package com.danialechoes.customerSystem.trace;

import org.slf4j.MDC;

import java.util.UUID;

public class TraceUtil {
    public static final String TRACE_ID_KEY = "traceId";
    public static final String SPAN_ID_KEY = "spanId";
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String SPAN_ID_HEADER = "X-Span-Id";

    public static String generateTraceId() {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 16);
    }

    public static String generateSpanId() {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 8);
    }

    public static void setTraceId(String traceId) {
        if (traceId != null && !traceId.isEmpty()) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    public static void setSpanId(String spanId) {
        if (spanId != null && !spanId.isEmpty()) {
            MDC.put(SPAN_ID_KEY, spanId);
        }
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    public static String getSpanId() {
        return MDC.get(SPAN_ID_KEY);
    }

    public static void clearTrace() {
        MDC.remove(TRACE_ID_KEY);
        MDC.remove(SPAN_ID_KEY);
    }

    public static void startTrace(String traceId) {
        setTraceId(traceId);
        setSpanId(generateSpanId());
    }

    public static void startTrace() {
        setTraceId(generateTraceId());
        setSpanId(generateSpanId());
    }

    public static void startSpan() {
        setSpanId(generateSpanId());
    }

}
