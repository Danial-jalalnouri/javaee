package com.danialechoes.web;

import com.danialechoes.dto.AccountCreationDTO;
import com.danialechoes.dto.TransactionDTO;
import com.danialechoes.entity.Account;
import com.danialechoes.entity.Currency;
import com.danialechoes.entity.Transaction;
import com.danialechoes.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management API", description = "APIs for managing bank accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new bank account")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountCreationDTO dto) {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @PostMapping("/{accountNumber}/deposit")
    @Operation(summary = "Deposit money into an account")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionDTO dto) {
        dto.setAccountNumber(accountNumber);
        return ResponseEntity.ok(accountService.deposit(dto));
    }

    @PostMapping("/{accountNumber}/withdraw")
    @Operation(summary = "Withdraw money from an account")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionDTO dto) {
        dto.setAccountNumber(accountNumber);
        return ResponseEntity.ok(accountService.withdraw(dto));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between accounts")
    public ResponseEntity<Void> transfer(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam BigDecimal amount) {
        accountService.transfer(fromAccount, toAccount, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all accounts for a customer")
    public ResponseEntity<List<Account>> getCustomerAccounts(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getCustomerAccounts(customerId));
    }

    @GetMapping("/customer/{customerId}/currency/{currency}")
    @Operation(summary = "Get customer accounts by currency")
    public ResponseEntity<List<Account>> getCustomerAccountsByCurrency(
            @PathVariable Long customerId,
            @PathVariable Currency currency) {
        return ResponseEntity.ok(accountService.getCustomerAccountsByCurrency(customerId, currency));
    }

    @GetMapping("/{accountNumber}/transactions")
    @Operation(summary = "Get transaction history for an account")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountTransactions(accountNumber));
    }
}
