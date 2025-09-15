package com.danialechoes.webclient.intergration;

import com.danialechoes.customerSystem.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "customerClient",
        url = "http://localhost:8080/api/customers",
        contextId = "customerClient")
public interface CustomerClient {

    @GetMapping
    List<CustomerDto> getAllCustomers();
    @GetMapping("/name/{name}")
    List<CustomerDto> getCustomersByName(@PathVariable("name") String name);

    @GetMapping("/{id}")
    CustomerDto getCustomerById(@PathVariable("id") Long id);
}
