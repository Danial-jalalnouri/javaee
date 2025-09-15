package com.danialechoes.depositSystem.dao.impl;

import com.danialechoes.depositSystem.dao.DepositDao;
import com.danialechoes.depositSystem.model.Deposit;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface DepositJpaDao extends JpaRepository<Deposit, Long>, DepositDao {
}
