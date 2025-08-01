package com.danialechoes.customerSystem.facade;

import com.danialechoes.customerSystem.dto.CustomerDto;
import com.danialechoes.customerSystem.exception.CustomerNotFoundException;
import com.danialechoes.customerSystem.mapper.CustomerMapper;
import com.danialechoes.customerSystem.model.Customer;
import com.danialechoes.customerSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerFacade {

    private final CustomerMapper mapper;
    private final CustomerService customerService;

    @Autowired
    public CustomerFacade(CustomerMapper mapper, CustomerService customerService) {
        this.mapper = mapper;
        this.customerService = customerService;
    }

    public CustomerDto addCustomer(CustomerDto customer) {
        Customer entity = mapper.toEntity(customer);
        entity = customerService.addCustomer(entity);
        return mapper.toDto(entity);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto updatedCustomer) {
        Customer entity = mapper.toEntity(updatedCustomer);
        entity = customerService.updateCustomer(id, entity);
        return entity != null ? mapper.toDto(entity) : null;
    }

    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
    }

    public CustomerDto getCustomerById(Long id) throws CustomerNotFoundException {
        Optional<Customer> customerById = customerService.getCustomerById(id);
        return customerById.map(mapper::toDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CustomerDto> getCustomersByName(String name) {
        return customerService.findByName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
