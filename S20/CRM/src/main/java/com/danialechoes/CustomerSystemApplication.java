package com.danialechoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Customer Management System API",
        version = "1.0",
        description = "REST API for managing customers"
    )
)
public class CustomerSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerSystemApplication.class, args);
    }
}
