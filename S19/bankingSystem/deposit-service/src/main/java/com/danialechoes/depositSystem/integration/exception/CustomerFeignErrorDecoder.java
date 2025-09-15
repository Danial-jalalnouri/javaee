package com.danialechoes.depositSystem.integration.exception;

import com.danialechoes.depositSystem.exception.CustomerNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomerFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new CustomerNotFoundException(
                    "Customer not found. Please check the customer ID or Name and try again."
            );
        }

        return defaultErrorDecoder.decode(methodKey, response);
    }
}
