package com.danialechoes.customerSystem.console;

import com.danialechoes.customerSystem.exception.CustomerNotFoundException;
import com.danialechoes.customerSystem.exception.DuplicateCustomerException;
import com.danialechoes.customerSystem.trace.TraceUtil;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@Profile("console")
public class MainConsoleInterface {

    private static final Logger logger = LoggerFactory.getLogger(MainConsoleInterface.class);
    private final CustomerConsole customerConsole;
    private final DepositConsole depositConsole;
    private final Scanner scanner;

    @Autowired
    public MainConsoleInterface(CustomerConsole customerConsole,
                                DepositConsole depositConsole) {
        this.customerConsole = customerConsole;
        this.depositConsole = depositConsole;
        this.scanner = new Scanner(System.in);
        logger.info("MainConsoleInterface initialized");
    }

    public void start() {
        logger.info("Starting Main Console Interface...");
        while (true) {
            System.out.println("\nBanking System");
            System.out.println("1. Customer Menu");
            System.out.println("2. Deposit Menu");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = Integer.parseInt(scanner.nextLine());

            TraceUtil.startTrace();

            logger.info("User selected choice: {}", choice);


            try {
                switch (choice) {
                    case 1 -> {
                        logger.info("Navigating to Customer Console...");
                        customerConsole.start();
                    }
                    case 2 -> {
                        logger.info("Navigating to Deposit Console...");
                        depositConsole.start();
                    }
                    case 0 -> {
                        logger.info("Exiting Main Console Interface...");
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (CustomerNotFoundException | DuplicateCustomerException e) {
                logger.error("Error occurred: {}", e.getMessage());
                System.out.println(e.getMessage());
            } catch (ConstraintViolationException ex) {
                logger.error("Validation error occurred: {}", ex.getMessage());
                String message = ex.getConstraintViolations().stream()
                        .map(violation -> "Property: " + violation.getPropertyPath() + ", Message: " + violation.getMessage())
                        .collect(Collectors.joining("\n "));
                System.out.println("Validation error: " + message);
            } catch (Exception e) {
                logger.error("An unexpected error occurred: {}", e.getMessage());
                System.out.println("An unexpected error occurred!");
            } finally {
                TraceUtil.clearTrace();
            }

        }
    }
}