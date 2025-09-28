package com.danialechoes.repository;

import com.danialechoes.entity.Account;
import com.danialechoes.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerIdAndCurrency(Long customerId, Currency currency);
}
