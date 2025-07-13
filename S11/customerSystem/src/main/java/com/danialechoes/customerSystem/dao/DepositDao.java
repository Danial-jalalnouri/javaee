package com.danialechoes.customerSystem.dao;

import com.danialechoes.customerSystem.model.Deposit;

import java.util.List;
import java.util.Optional;

public interface DepositDao {
    Deposit save(Deposit deposit);

    Optional<Deposit> findById(Long id);

    void deleteById(Long id);

    List<Deposit> findByCustomerId(Long id);

    List<Deposit> findAll();
}
