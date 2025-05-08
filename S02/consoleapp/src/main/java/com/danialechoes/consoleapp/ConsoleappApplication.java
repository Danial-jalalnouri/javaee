package com.danialechoes.consoleapp;

import com.danialechoes.consoleapp.service.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ConsoleappApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ConsoleappApplication.class, args);

		HelloService helloService = context.getBean(HelloService.class);
		helloService.hello();
		//HelloService instance = HelloService.getInstance();
		//instance.hello();
	}

}
