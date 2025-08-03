package com.danialechoes.customerSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // Swagger configuration can be added here
    // For example, you can configure Docket bean for Swagger UI
    // and set up API documentation properties
    // This is a placeholder for now, as the actual configuration
    // would depend on your specific requirements and setup

    @Bean
    public OpenAPI SystemOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Deposit API")
                        .version("1.0")
                        .description("API documentation for the Deposit application"));
    }
}
