package com.danialechoes.web;

import com.danialechoes.dto.LegalCustomerDTO;
import com.danialechoes.dto.RealCustomerDTO;
import com.danialechoes.entity.LegalCustomer;
import com.danialechoes.entity.RealCustomer;
import com.danialechoes.mapper.CustomerMapper;
import com.danialechoes.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping("/real")
    @Operation(summary = "Create a real customer")
    public ResponseEntity<RealCustomerDTO> createRealCustomer(@Valid @RequestBody RealCustomerDTO dto) {
        RealCustomer entity = customerMapper.toEntity(dto);
        entity = (RealCustomer) customerService.save(entity);
        return ResponseEntity.ok(customerMapper.toDto(entity));
    }

    @PostMapping("/legal")
    @Operation(summary = "Create a legal customer")
    public ResponseEntity<LegalCustomerDTO> createLegalCustomer(@Valid @RequestBody LegalCustomerDTO dto) {
        LegalCustomer entity = customerMapper.toEntity(dto);
        entity = (LegalCustomer) customerService.save(entity);
        return ResponseEntity.ok(customerMapper.toDto(entity));
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<?>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll().stream()
                .map(customer -> {
                    if (customer instanceof RealCustomer) {
                        return customerMapper.toDto((RealCustomer) customer);
                    } else {
                        return customerMapper.toDto((LegalCustomer) customer);
                    }
                })
                .collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by name")
    public ResponseEntity<List<?>> searchCustomers(@RequestParam String name) {
        return ResponseEntity.ok(customerService.findByName(name).stream()
                .map(customer -> {
                    if (customer instanceof RealCustomer) {
                        return customerMapper.toDto((RealCustomer) customer);
                    } else {
                        return customerMapper.toDto((LegalCustomer) customer);
                    }
                })
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer by ID")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/real/{id}")
    @Operation(summary = "Update a real customer")
    public ResponseEntity<?> updateRealCustomer(
            @PathVariable Long id,
            @Valid @RequestBody RealCustomerDTO dto) {
        return customerService.findById(id)
                .map(customer -> {
                    if (customer instanceof RealCustomer realCustomer) {
                        customerMapper.updateRealCustomer(realCustomer, dto);
                        realCustomer = (RealCustomer) customerService.save(realCustomer);
                        return ResponseEntity.ok(customerMapper.toDto(realCustomer));
                    }
                    return ResponseEntity.<RealCustomerDTO>badRequest().build();
                })
                .orElse(ResponseEntity.<RealCustomerDTO>notFound().build());
    }

    @PutMapping("/legal/{id}")
    @Operation(summary = "Update a legal customer")
    public ResponseEntity<?> updateLegalCustomer(
            @PathVariable Long id,
            @Valid @RequestBody LegalCustomerDTO dto) {
        return customerService.findById(id)
                .map(customer -> {
                    if (customer instanceof LegalCustomer legalCustomer) {
                        customerMapper.updateLegalCustomer(legalCustomer, dto);
                        legalCustomer = (LegalCustomer) customerService.save(legalCustomer);
                        return ResponseEntity.ok(customerMapper.toDto(legalCustomer));
                    }
                    return ResponseEntity.<LegalCustomerDTO>badRequest().build();
                })
                .orElse(ResponseEntity.<LegalCustomerDTO>notFound().build());
    }
}
