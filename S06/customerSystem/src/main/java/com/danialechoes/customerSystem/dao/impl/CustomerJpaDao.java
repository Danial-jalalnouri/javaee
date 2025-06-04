package com.danialechoes.customerSystem.dao.impl;

import com.danialechoes.customerSystem.dao.CustomerDao;
import com.danialechoes.customerSystem.model.Customer;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
@Profile("jpa")
public interface CustomerJpaDao extends JpaRepository<Customer, Long>, CustomerDao {
}
