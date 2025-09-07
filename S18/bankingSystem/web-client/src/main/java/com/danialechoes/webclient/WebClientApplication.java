package com.danialechoes.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.danialechoes.webclient.intergration")
public class WebClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebClientApplication.class, args);
    }
}
