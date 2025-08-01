package com.danialechoes.customerSystem.controller;

import com.danialechoes.customerSystem.dto.RealCustomerDto;
import com.danialechoes.customerSystem.exception.CustomerNotFoundException;
import com.danialechoes.customerSystem.facade.CustomerFacade;
import com.danialechoes.customerSystem.model.CustomerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CustomerController.class)
@Import(CustomerControllerTest.MockServiceConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerFacade customerFacade;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        public CustomerFacade customerFacade() {
            return mock(CustomerFacade.class);
        }
    }

    @Test
    void getCustomerById() throws Exception {
        RealCustomerDto customerDto = new RealCustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
        customerDto.setPhoneNumber("1234567890");
        customerDto.setType(CustomerType.REAL);

        when(customerFacade.getCustomerById(1L)).thenReturn(customerDto);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"))
                .andExpect(jsonPath("$.type").value(CustomerType.REAL.name()));

    }

    @Test
    void getCustomerById_NotFound() throws Exception {
        when(customerFacade.getCustomerById(999L))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerById_BadRequest() throws Exception {
        mockMvc.perform(get("/api/customers/invalid"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomersByName() throws Exception {
        RealCustomerDto customerDto = new RealCustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
        customerDto.setPhoneNumber("1234567890");
        customerDto.setType(CustomerType.REAL);

        when(customerFacade.getCustomersByName("John Doe")).thenReturn(List.of(customerDto));

        String name = "John Doe";
        mockMvc.perform(get("/api/customers/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @Test
    void getCustomersByName_NotFound() throws Exception {
        String name = "NonExistent";
        when(customerFacade.getCustomersByName(name)).thenThrow(CustomerNotFoundException.class);

        mockMvc.perform(get("/api/customers/name/{name}", name))
                .andExpect(status().isNotFound());
    }

}
