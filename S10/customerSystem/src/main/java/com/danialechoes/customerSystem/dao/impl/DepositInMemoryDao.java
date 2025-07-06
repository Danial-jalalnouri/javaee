package com.danialechoes.customerSystem.dao.impl;

import com.danialechoes.customerSystem.dao.DepositDao;
import com.danialechoes.customerSystem.exception.DepositNotFoundException;
import com.danialechoes.customerSystem.model.Deposit;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("memo")
public class DepositInMemoryDao implements DepositDao {

    private final Map<Long, Deposit> deposits = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0);

    @Override
    public Deposit save(Deposit deposit) {
        if (deposit.getId() == null) {
            long id = currentId.incrementAndGet();
            deposit.setId(id);
        }
        deposits.put(deposit.getId(), deposit);
        return deposit;
    }

    @Override
    public Optional<Deposit> findById(Long id) {
        if (!deposits.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(deposits.get(id));
    }

    @Override
    public void deleteById(Long id) {
        if (deposits.containsKey(id)) {
            deposits.remove(id);
        } else {
            throw new DepositNotFoundException("Deposit with id " + id + " does not exist.");
        }
    }

    @Override
    public List<Deposit> findByCustomerId(Long id) {
        return deposits.values().stream()
                .filter(deposit -> deposit.getCustomer() != null && deposit.getCustomer().getId().equals(id))
                .toList();
    }

    @Override
    public List<Deposit> findAll() {
        return deposits.values().stream().toList();
    }
}
