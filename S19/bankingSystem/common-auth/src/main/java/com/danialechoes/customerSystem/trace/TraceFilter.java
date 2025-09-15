package com.danialechoes.customerSystem.trace;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1) // Ensure this filter runs first
public class TraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        try{
            String traceId = httpRequest.getHeader(TraceUtil.TRACE_ID_HEADER);
            String spanId = httpRequest.getHeader(TraceUtil.SPAN_ID_HEADER);

            if(traceId != null && !traceId.isEmpty()) {
                TraceUtil.startTrace(traceId);
            } else {
                TraceUtil.startTrace();
            }

            httpResponse.setHeader(TraceUtil.TRACE_ID_HEADER, TraceUtil.getTraceId());
            httpResponse.setHeader(TraceUtil.SPAN_ID_HEADER, TraceUtil.getSpanId());

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            TraceUtil.clearTrace();
        }
    }
}
