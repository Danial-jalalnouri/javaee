package com.danialechoes.customerSystem.dao.impl;

import com.danialechoes.customerSystem.dao.DepositDao;
import com.danialechoes.customerSystem.model.Deposit;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface DepositJpaDao extends JpaRepository<Deposit, Long>, DepositDao {
}
