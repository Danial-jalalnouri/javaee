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
        title = "Accounting System API",
        version = "1.0",
        description = "REST API for managing accounts and transactions"
    )
)
public class AccountingServiceApplication {
    public static void main(String[] args) {
        // Set working directory to project root
        Path currentPath = Paths.get("").toAbsolutePath();
        String rootPath = currentPath.getParent().toString();
        System.setProperty("user.dir", rootPath);

        SpringApplication.run(AccountingServiceApplication.class, args);
    }
}
