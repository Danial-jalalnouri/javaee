package com.danialechoes.consoleapp.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

/*    private static final HelloService INSTANCE;
    static {
        INSTANCE = new HelloService();
    }
    private HelloService(){

    }
    public static HelloService getInstance(){
        return INSTANCE;
    }*/
    public void hello() {
        System.out.println("Hello world");
    }
}
