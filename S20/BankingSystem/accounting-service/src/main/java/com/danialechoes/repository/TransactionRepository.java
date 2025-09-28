package com.danialechoes.repository;

import com.danialechoes.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccountOrderByTransactionDateDesc(String fromAccount, String toAccount);
}
