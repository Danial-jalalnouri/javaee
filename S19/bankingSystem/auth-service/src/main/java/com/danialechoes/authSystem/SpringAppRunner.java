package com.danialechoes.authSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {
        "com.danialechoes.authSystem",
        "com.danialechoes.customerSystem",
        "com.danialechoes.depositSystem",
        "com.danialechoes.transactionSystem"
})
public class SpringAppRunner {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringAppRunner.class);
        app.run(args);
    }

}
