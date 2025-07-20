package com.danialechoes.customerSystem.service;

import com.danialechoes.customerSystem.dao.CustomerDao;
import com.danialechoes.customerSystem.dao.DepositDao;
import com.danialechoes.customerSystem.exception.CustomerNotFoundException;
import com.danialechoes.customerSystem.exception.DepositNotFoundException;
import com.danialechoes.customerSystem.exception.InsufficientFundsException;
import com.danialechoes.customerSystem.model.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DepositService {
    private final DepositDao depositDao;
    private final CustomerDao customerDao;

    @Autowired
    public DepositService(DepositDao depositDao, CustomerDao customerDao) {
        this.depositDao = depositDao;
        this.customerDao = customerDao;
    }

    public Deposit addDeposit(Long customerId) {
        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.ZERO); // Initialize with zero amount
        deposit.setCustomer(customerDao.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + customerId + " does not exist.")));
        return depositDao.save(deposit);
    }

    public Deposit addDeposit(Deposit deposit) {
        return depositDao.save(deposit);
    }

    public Deposit depositAmount(Long id, BigDecimal amount) {
        for(int i = 0; i < 10; i++) {
            try {
                Deposit deposit = depositDao.findById(id)
                        .orElseThrow(() -> new DepositNotFoundException("Deposit with id " + id + " does not exist."));
                deposit.setAmount(deposit.getAmount().add(amount));
                return depositDao.save(deposit);
            } catch (ObjectOptimisticLockingFailureException e) {
                try {
                    Thread.sleep(100); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    throw new RuntimeException("Thread was interrupted while waiting to retry deposit operation.", ie);
                }
            }
        }
        throw new RuntimeException("Failed to deposit amount after multiple retries due to concurrent modifications.");
    }

    @Transactional(rollbackFor = InsufficientFundsException.class)
    public Deposit withdrawAmount(Long id, BigDecimal amount) {
        Deposit deposit = depositDao.findById(id)
                .orElseThrow(() -> new DepositNotFoundException("Deposit with id " + id + " does not exist."));
        deposit.setAmount(deposit.getAmount().subtract(amount));
        if (deposit.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Insufficient funds in deposit with id " + id);
        }
        return depositDao.save(deposit);
    }

    public void deleteDeposit(Long id) {
        if (depositDao.findById(id).isPresent()) {
            depositDao.deleteById(id);
        } else {
            throw new DepositNotFoundException("Deposit with id " + id + " does not exist.");
        }
    }

    public List<Deposit> getDepositsByCustomerId(Long customerId) {
        return depositDao.findByCustomerId(customerId);
    }

    public List<Deposit> getAllDeposits() {
        return depositDao.findAll();
    }


}
