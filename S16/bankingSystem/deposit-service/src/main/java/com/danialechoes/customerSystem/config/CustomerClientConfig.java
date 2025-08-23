package com.danialechoes.customerSystem.config;

import com.danialechoes.customerSystem.integration.AuthClient;
import com.danialechoes.customerSystem.integration.ClientAuthInterceptor;
import com.danialechoes.customerSystem.integration.exception.CustomerFeignErrorDecoder;
import com.danialechoes.customerSystem.trace.TraceRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerClientConfig {
    @Bean
    public ErrorDecoder customerErrorDecoder() {
        return new CustomerFeignErrorDecoder();
    }

    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean("customerClientAuthInterceptor")
    public RequestInterceptor customerClientAuthInterceptor(AuthClient authClient) {
        return new ClientAuthInterceptor(authClient);
    }

    @Bean("traceRequestInterceptor")
    public RequestInterceptor traceRequestInterceptor() {
        return new TraceRequestInterceptor();
    }
}
