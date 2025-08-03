package com.danialechoes.customerSystem.config;

import com.danialechoes.customerSystem.integration.exception.CustomerFeignErrorDecoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerClientConfig {
    @Bean
    public ErrorDecoder customerErrorDecoder() {
        return new CustomerFeignErrorDecoder();
    }

    @Bean
    public Encoder feignFormEncoder() {
        return new feign.form.spring.SpringFormEncoder();
    }
}
