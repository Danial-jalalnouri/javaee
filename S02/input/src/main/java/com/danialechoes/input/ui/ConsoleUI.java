package com.danialechoes.input.ui;

import com.danialechoes.input.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleUI {
    //@Autowired
    private final GreetingService greetingService;
    //private GreetingService greetingService = GreetingService.getInstance();

    @Autowired
    public ConsoleUI(GreetingService greetingService){
        this.greetingService = greetingService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        String message = greetingService.greet(name);
        System.out.println(message);
    }
}
