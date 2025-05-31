package com.danialechoes.customerSystem.dao;

import com.danialechoes.customerSystem.model.Customer;

import java.util.List;

public interface CustomerDao {
    Customer save(Customer customer);
    Customer update(Long id, Customer customer);
    boolean delete(Long id);
    Customer findById(Long id);
    List<Customer> findAll();
    boolean existsById(Long id);
}