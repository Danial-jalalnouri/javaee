package com.danialechoes.webclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deposit")
public class DepositWebController {

    @GetMapping
    public String depositMenu() {
        return "deposit-menu";
    }

}
