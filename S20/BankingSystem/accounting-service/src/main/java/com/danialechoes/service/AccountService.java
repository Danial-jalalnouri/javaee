package com.danialechoes.service;

import com.danialechoes.dto.AccountCreationDTO;
import com.danialechoes.dto.TransactionDTO;
import com.danialechoes.entity.Account;
import com.danialechoes.entity.Currency;
import com.danialechoes.entity.Transaction;
import com.danialechoes.entity.TransactionType;
import com.danialechoes.repository.AccountRepository;
import com.danialechoes.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Account createAccount(AccountCreationDTO dto) {
        Account account = new Account();
        account.setCustomerId(dto.getCustomerId());
        account.setCurrency(dto.getCurrency());
        account.setBalance(dto.getInitialBalance());
        account.setAccountNumber(generateAccountNumber());

        Account savedAccount = accountRepository.save(account);

        // Record initial deposit transaction
        if (dto.getInitialBalance().compareTo(BigDecimal.ZERO) > 0) {
            Transaction transaction = new Transaction();
            transaction.setFromAccount("INITIAL_DEPOSIT");
            transaction.setToAccount(account.getAccountNumber());
            transaction.setAmount(dto.getInitialBalance());
            transaction.setType(TransactionType.DEPOSIT);
            transaction.setCurrency(dto.getCurrency());
            transaction.setDescription("Initial deposit");
            transactionRepository.save(transaction);
        }

        return savedAccount;
    }

    @Transactional
    public Account deposit(TransactionDTO dto) {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(dto.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setToAccount(dto.getAccountNumber());
        transaction.setFromAccount("DEPOSIT");
        transaction.setAmount(dto.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription("Deposit to account");
        transactionRepository.save(transaction);

        return updatedAccount;
    }

    @Transactional
    public Account withdraw(TransactionDTO dto) {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(dto.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(dto.getAccountNumber());
        transaction.setToAccount("WITHDRAWAL");
        transaction.setAmount(dto.getAmount());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setCurrency(account.getCurrency());
        transaction.setDescription("Withdrawal from account");
        transactionRepository.save(transaction);

        return updatedAccount;
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            throw new RuntimeException("Cannot transfer between accounts with different currencies");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccountNumber);
        transaction.setToAccount(toAccountNumber);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setCurrency(fromAccount.getCurrency());
        transaction.setDescription("Transfer between accounts");
        transactionRepository.save(transaction);
    }

    public List<Account> getCustomerAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public List<Account> getCustomerAccountsByCurrency(Long customerId, Currency currency) {
        return accountRepository.findByCustomerIdAndCurrency(customerId, currency);
    }

    public List<Transaction> getAccountTransactions(String accountNumber) {
        return transactionRepository.findByFromAccountOrToAccountOrderByTransactionDateDesc(accountNumber, accountNumber);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
