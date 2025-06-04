package com.danialechoes.customerSystem.service;

import com.danialechoes.customerSystem.dao.CustomerDao;
import com.danialechoes.customerSystem.model.Customer;
import com.danialechoes.customerSystem.model.CustomerType;
import com.danialechoes.customerSystem.model.RealCustomer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @PostConstruct
    public void init() {
        // Initialize some sample customers
        addCustomer(RealCustomer.builder()
                .id(null)
                .name("Alice")
                .phoneNumber("5551234567")
                .type(CustomerType.REAL)
                .family("Johnson")
                .build());
    }

    public Customer addCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        if (customerDao.existsById(id)) {
            updatedCustomer.setId(id); // Ensure the ID is set for the update
            return customerDao.save(updatedCustomer);
        }
        return null; // or throw an exception
    }

    public boolean deleteCustomer(Long id) {
        if (customerDao.existsById(id)) {
            customerDao.deleteById(id);
            return true;
        }
        return false; // or throw an exception
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerDao.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public List<Customer> findByName(String name){
        return customerDao.findByNameIgnoreCase(name);
    }
}
