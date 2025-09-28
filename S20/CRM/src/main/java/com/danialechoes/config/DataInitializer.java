package com.danialechoes.config;

import com.danialechoes.entity.LegalCustomer;
import com.danialechoes.entity.RealCustomer;
import com.danialechoes.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(CustomerRepository customerRepository) {
        return args -> {
            RealCustomer realCustomer = new RealCustomer();
            realCustomer.setName("John");
            realCustomer.setNumber("123456");
            realCustomer.setFamily("Doe");
            customerRepository.save(realCustomer);

            LegalCustomer legalCustomer = new LegalCustomer();
            legalCustomer.setName("Tech Corp");
            legalCustomer.setNumber("789012");
            legalCustomer.setFax("555-0123");
            customerRepository.save(legalCustomer);
        };
    }
}
