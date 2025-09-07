package com.danialechoes.webclient.controller;

import com.danialechoes.customerSystem.dto.CustomerDto;
import com.danialechoes.webclient.intergration.CustomerClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerWebController {

    private final CustomerClient customerClient;

    @Autowired
    public CustomerWebController(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @GetMapping
    public String customerMenu() {
        return "customer-menu";
    }

    @GetMapping("/list")
    public String listCustomers(Model model) {
        List<CustomerDto> customers = customerClient.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customer-list";
    }

    @GetMapping("/view")
    public String viewCustomer() {
        return "customer-view";
    }

    @PostMapping("/view")
    public String viewCustomer(@RequestParam Long cid, Model model) {
        try {
            model.addAttribute("customer", customerClient.getCustomerById(cid));
        } catch (FeignException.NotFound ex) {
            model.addAttribute("error", "Customer with ID " + cid + " not found.");
        }
        return "customer-view";
    }

    @GetMapping("/find")
    public String findCustomerByName() {
        return "customer-find";
    }

    @PostMapping("/find")
    public String findCustomerByName(@RequestParam String name, Model model) {
        try{
            List<CustomerDto> customers = customerClient.getCustomersByName(name);
            model.addAttribute("customers", customers);
        } catch (FeignException.NotFound ex){
            model.addAttribute("error", "No customers found with the name: " + name);
        }
        return "customer-list";
    }

}
