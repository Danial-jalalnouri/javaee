package com.danialechoes.input.service;

import com.danialechoes.input.config.GreetingProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

/*    private static final GreetingService INSTANCE;

    static {
        INSTANCE = new GreetingService();
    }

    private GreetingService()
    {

    }

    public static GreetingService getInstance(){
        return INSTANCE;
    }*/

    //@Value("${greeting.message}")
    //private String greetingMessage;

    private final GreetingProperties greetingProperties;

    public GreetingService(GreetingProperties greetingProperties) {
        this.greetingProperties = greetingProperties;
    }

    public String greet(String name){
        return greetingProperties.getMessage() + " " + name + "!";
    }

}
