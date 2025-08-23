package com.danialechoes.customerSystem.trace;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class TraceRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String traceId = TraceUtil.getTraceId();
        String spanId = TraceUtil.getSpanId();

        if (traceId != null) {
            template.header(TraceUtil.TRACE_ID_HEADER, traceId);
        }

        if (spanId != null) {
            template.header(TraceUtil.SPAN_ID_HEADER, spanId);
        }
    }
}
