package com.danialechoes.customerSystem.dao.impl;

import com.danialechoes.customerSystem.model.Deposit;
import com.danialechoes.customerSystem.model.LegalCustomer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"jdbc"})
public class DepositJdbcDaoIntegrationTest {

    @Autowired
    private DepositJdbcDao depositJdbcDao;

    @Autowired
    private CustomerJdbcDao customerJdbcDao;

    @AfterEach
    void cleanUp() {
        depositJdbcDao.findAll().forEach(deposit -> {
            depositJdbcDao.deleteById(deposit.getId());
        });
        customerJdbcDao.findAll().forEach(customer -> {
            customerJdbcDao.deleteById(customer.getId());
        });
    }

    @Test
    void saveAndFindById() {
        // This test should create a deposit, save it, and then retrieve it by ID.
        // Implement the logic to create a deposit and verify its retrieval.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Test Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);
        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(1000.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);
        Deposit foundDeposit = depositJdbcDao.findById(deposit.getId())
                .orElseThrow(() -> new RuntimeException("Deposit not found"));
        assert foundDeposit != null;
        assert foundDeposit.getAmount().compareTo(BigDecimal.valueOf(1000.0)) == 0;
        assert foundDeposit.getCustomer().getId().equals(customer.getId());
        assert foundDeposit.getCustomer().getName().equals("Test Company");
        assert foundDeposit.getCustomer().getPhoneNumber().equals("1234567890");
    }

    @Test
    void deleteById(){
        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(500.0));
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Test Company");
        customer.setPhoneNumber("1234567890");
        customerJdbcDao.save(customer);
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);
        assert deposit.getId() != null : "Deposit ID should not be null after saving";
        Long depositId = deposit.getId();
        depositJdbcDao.deleteById(depositId);
        Optional<Deposit> byId = depositJdbcDao.findById(depositId);
        assert byId.isEmpty() : "Deposit should not be found after deletion";
    }

    @Test
    void findByCustomerId() {
        // This test should create a deposit for a specific customer and then retrieve it by customer ID.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Test Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);

        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(2000.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);

        assert deposit.getId() != null : "Deposit ID should not be null after saving";

        var deposits = depositJdbcDao.findByCustomerId(customer.getId());
        assert !deposits.isEmpty() : "Deposits should not be empty for the given customer ID";
        assert deposits.get(0).getAmount().compareTo(BigDecimal.valueOf(2000.0)) == 0 : "Deposit amount should match the saved amount";
    }

    @Test
    void findAll() {
        // This test should create multiple deposits and verify that they can be retrieved.
        LegalCustomer customer1 = new LegalCustomer();
        customer1.setName("Company A");
        customer1.setPhoneNumber("1112223333");
        customer1.setFax("333222111");
        customer1 = (LegalCustomer) customerJdbcDao.save(customer1);

        LegalCustomer customer2 = new LegalCustomer();
        customer2.setName("Company B");
        customer2.setPhoneNumber("4445556666");
        customer2.setFax("666555444");
        customer2 = (LegalCustomer) customerJdbcDao.save(customer2);

        Deposit deposit1 = new Deposit();
        deposit1.setAmount(BigDecimal.valueOf(3000.0));
        deposit1.setCustomer(customer1);
        depositJdbcDao.save(deposit1);

        Deposit deposit2 = new Deposit();
        deposit2.setAmount(BigDecimal.valueOf(4000.0));
        deposit2.setCustomer(customer2);
        depositJdbcDao.save(deposit2);

        var deposits = depositJdbcDao.findAll();
        assert deposits.size() == 2 : "There should be two deposits in the database";
    }

    @Test
    void updateDeposit() {
        // This test should create a deposit, update it, and verify the changes.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Update Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);

        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(1500.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);

        assert deposit.getId() != null : "Deposit ID should not be null after saving";

        // Update the deposit amount
        deposit.setAmount(BigDecimal.valueOf(2000.0));
        deposit = depositJdbcDao.save(deposit);

        Deposit updatedDeposit = depositJdbcDao.findById(deposit.getId())
                .orElseThrow(() -> new RuntimeException("Updated Deposit not found"));

        assert updatedDeposit.getAmount().compareTo(BigDecimal.valueOf(2000.0)) == 0 : "Updated amount should be 2000.0";
    }

    @Test
    void checkInitialVersion() {
        // This test should create a deposit and check the initial version field.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Initial Version Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);

        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(1000.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);

        assertEquals(0L, deposit.getVersion(), "Initial version should be 0");
    }

    @Test
    void checkWrongVersion() {
        // This test should create a deposit and then try to update it with a wrong version.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Wrong Version Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);

        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(1500.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);

        assert deposit.getId() != null : "Deposit ID should not be null after saving";

        // Simulate a wrong version update
        try {
            deposit.setVersion(999L); // Set an incorrect version
            depositJdbcDao.save(deposit);
            assert false : "Expected an ObjectOptimisticLockingFailureException due to wrong version";
        } catch (ObjectOptimisticLockingFailureException e) {
            assert true;
        }
    }

    @Test
    void checkVersionUpdate() {
        // This test should create a deposit, update it, and check the version field.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Version Test Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);

        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(2500.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);

        assert deposit.getId() != null : "Deposit ID should not be null after saving";
        Long initialVersion = deposit.getVersion();

        // Update the deposit amount
        deposit.setAmount(BigDecimal.valueOf(3000.0));
        deposit = depositJdbcDao.save(deposit);

        assertEquals(1L, deposit.getVersion(), "Version should be incremented to 2 after update");
    }

    @Test
    void checkVersionUpdatedAfterFindById() {
        // This test should create a deposit, retrieve it, and check the version field after retrieval.
        LegalCustomer customer = new LegalCustomer();
        customer.setName("Version Check Company");
        customer.setPhoneNumber("1234567890");
        customer.setFax("987654321");
        customer = (LegalCustomer) customerJdbcDao.save(customer);

        Deposit deposit = new Deposit();
        deposit.setAmount(BigDecimal.valueOf(3500.0));
        deposit.setCustomer(customer);
        deposit = depositJdbcDao.save(deposit);

        assert deposit.getId() != null : "Deposit ID should not be null after saving";
        Long initialVersion = deposit.getVersion();

        // Retrieve the deposit by ID
        Deposit retrievedDeposit = depositJdbcDao.findById(deposit.getId())
                .orElseThrow(() -> new RuntimeException("Deposit not found after saving"));

        assertEquals(initialVersion, retrievedDeposit.getVersion(), "Version should remain the same after retrieval");
    }

}
