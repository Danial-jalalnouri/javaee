package com.danialechoes.customerSystem.service;

import com.danialechoes.customerSystem.dao.CustomerDao;
import com.danialechoes.customerSystem.dao.DepositDao;
import com.danialechoes.customerSystem.exception.CustomerNotFoundException;
import com.danialechoes.customerSystem.exception.DepositNotFoundException;
import com.danialechoes.customerSystem.exception.InsufficientFundsException;
import com.danialechoes.customerSystem.model.Customer;
import com.danialechoes.customerSystem.model.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        deposit.setAmount(0.0); // Initialize with zero amount
        deposit.setCustomer(customerDao.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + customerId + " does not exist.")));
        return depositDao.save(deposit);
    }

    public Deposit addDeposit(Deposit deposit) {
        return depositDao.save(deposit);
    }

    public Deposit depositAmount(Long id, Double amount) {
        Deposit deposit = depositDao.findById(id)
                .orElseThrow(() -> new DepositNotFoundException("Deposit with id " + id + " does not exist."));
        deposit.setAmount(deposit.getAmount() + amount);
        return depositDao.save(deposit);
    }

    public Deposit withdrawAmount(Long id, Double amount) {
        Deposit deposit = depositDao.findById(id)
                .orElseThrow(() -> new DepositNotFoundException("Deposit with id " + id + " does not exist."));
        if (deposit.getAmount() < amount) {
            throw new InsufficientFundsException("Insufficient funds in deposit with id " + id);
        }
        deposit.setAmount(deposit.getAmount() - amount);
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
