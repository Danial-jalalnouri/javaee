package com.danialechoes.customerSystem.controller;

import com.danialechoes.customerSystem.dto.LegalCustomerDto;
import com.danialechoes.customerSystem.dto.CustomerDto;
import com.danialechoes.customerSystem.dto.RealCustomerDto;
import com.danialechoes.customerSystem.facade.CustomerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerFacade customerFacade;

    @Autowired
    public CustomerController(CustomerFacade customerFacade) {
        this.customerFacade = customerFacade;
    }

    // This is a simple controller class that handles HTTP requests

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerFacade.getAllCustomers();
    }

    @Operation(summary = "Get customers by name", description = "Retrieve a list of customers by their name")
    @GetMapping("/name/{name}")
    public List<CustomerDto> getCustomersByName(@PathVariable String name) {
        return customerFacade.getCustomersByName(name);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by its unique identifier")
    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id) {
        return customerFacade.getCustomerById(id);
    }

    @Operation(summary = "Add a new customer", description = "Create a new customer")
    @PostMapping
    public CustomerDto addCustomer(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer object to be added",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            RealCustomerDto.class,
                                            LegalCustomerDto.class
                                    }
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Real Customer Example",
                                            value = "{"
                                                    + "\"name\": \"John Doe\","
                                                    + "\"phoneNumber\": \"+1234567890\","
                                                    + "\"type\": \"REAL\","
                                                    + "\"family\": \"Doe\""
                                                    + "}"
                                    ),
                                    @ExampleObject(
                                            name = "Legal Customer Example",
                                            value = "{"
                                                    + "\"name\": \"John Doe\","
                                                    + "\"phoneNumber\": \"+1234567890\","
                                                    + "\"type\": \"LEGAL\","
                                                    + "\"fax\": \"+0987654321\""
                                                    + "}"
                                    )
                            }
                    )
            )
            @RequestBody CustomerDto customer
    ) {
        return customerFacade.addCustomer(customer);
    }

    @Operation(summary = "Update an existing customer", description = "Update the details of an existing customer")
    @PutMapping("/{id}")
    public CustomerDto updateCustomer(@PathVariable Long id,
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                            description = "Updated customer object",
                                            required = true,
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            oneOf = {
                                                                    RealCustomerDto.class,
                                                                    LegalCustomerDto.class
                                                            }
                                                    ),
                                                    examples = {
                                                            @ExampleObject(
                                                                    name = "Real Customer Example",
                                                                    value = "{"
                                                                            + "\"name\": \"John Doe\","
                                                                            + "\"phoneNumber\": \"+1234567890\","
                                                                            + "\"type\": \"REAL\","
                                                                            + "\"family\": \"Doe\""
                                                                            + "}"
                                                            ),
                                                            @ExampleObject(
                                                                    name = "Legal Customer Example",
                                                                    value = "{"
                                                                            + "\"name\": \"John Doe\","
                                                                            + "\"phoneNumber\": \"+1234567890\","
                                                                            + "\"type\": \"LEGAL\","
                                                                            + "\"fax\": \"+0987654321\""
                                                                            + "}"
                                                            )
                                                    }
                                            )
                                    )
                                    @RequestBody CustomerDto updatedCustomer
    ) {
        return customerFacade.updateCustomer(id, updatedCustomer);
    }

    @Operation(summary = "Delete a customer", description = "Remove a customer from the customer system")
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerFacade.deleteCustomer(id);
        if (deleted) {
            return "Customer deleted successfully";
        } else {
            return "Customer not found";
        }
    }
}
