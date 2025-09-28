package com.danialechoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        // Set working directory to project root
        Path currentPath = Paths.get("").toAbsolutePath();
        String rootPath = currentPath.getParent().toString();
        System.setProperty("user.dir", rootPath);

        SpringApplication.run(CustomerSystemApplication.class, args);
    }
}
