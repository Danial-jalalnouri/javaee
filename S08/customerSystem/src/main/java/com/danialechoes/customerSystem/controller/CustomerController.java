package com.danialechoes.customerSystem.controller;

import com.danialechoes.customerSystem.dto.ErrorResponse;
import com.danialechoes.customerSystem.dto.LegalCustomerDto;
import com.danialechoes.customerSystem.dto.CustomerDto;
import com.danialechoes.customerSystem.dto.RealCustomerDto;
import com.danialechoes.customerSystem.exception.CustomerNotFoundException;
import com.danialechoes.customerSystem.facade.CustomerFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerFacade.getAllCustomers());
    }

    @Operation(summary = "Get customers by name", description = "Retrieve a list of customers by their name")
    @GetMapping("/name/{name}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers found",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            oneOf = {
                                                    RealCustomerDto.class,
                                                    LegalCustomerDto.class
                                            }
                                    )
                            )
                    )),
            @ApiResponse(responseCode = "404",
                    description = "No customers found with the given name", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            ))
    })
    public ResponseEntity<?> getCustomersByName(@PathVariable String name) {
        List<CustomerDto> customers = customerFacade.getCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by its unique identifier")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            RealCustomerDto.class,
                                            LegalCustomerDto.class
                                    }
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    public ResponseEntity<?> getCustomerById(@PathVariable @Positive(message = "id should be positive") Long id) {
        CustomerDto customerDto = customerFacade.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(summary = "Add a new customer", description = "Create a new customer")
    @PostMapping
    public ResponseEntity<CustomerDto> addCustomer(
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
            @Valid @RequestBody CustomerDto customer
    ) {
        return ResponseEntity.ok(customerFacade.addCustomer(customer));
    }

    @Operation(summary = "Update an existing customer", description = "Update the details of an existing customer")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id,
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
        return ResponseEntity.ok(customerFacade.updateCustomer(id, updatedCustomer));
    }

    @Operation(summary = "Delete a customer", description = "Remove a customer from the customer system")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    ))
    })
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerFacade.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Customer deleted successfully");
    }
}
