package com.danialechoes.plugin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PluginApplication {

	public static void main(String[] args) {
		SpringApplication.run(PluginApplication.class, args);

		System.out.println("Hello");
	}

}
