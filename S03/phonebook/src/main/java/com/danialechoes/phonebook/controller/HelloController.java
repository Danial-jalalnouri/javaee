package com.danialechoes.phonebook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
// This is a simple controller class that handles HTTP requests
public class HelloController {
    @GetMapping("/world")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/name/{name}")
    public String helloByName(@PathVariable String name) {
        return "Hello, " + name;
    }
}
