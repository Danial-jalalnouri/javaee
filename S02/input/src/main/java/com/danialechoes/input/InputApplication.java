package com.danialechoes.input;

import com.danialechoes.input.ui.ConsoleUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class InputApplication implements CommandLineRunner {

	private final ConsoleUI consoleUI;

	@Autowired
	public InputApplication(ConsoleUI consoleUI){
		this.consoleUI = consoleUI;
	}

	public static void main(String[] args) {
		SpringApplication.run(InputApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		consoleUI.start();
	}
}
