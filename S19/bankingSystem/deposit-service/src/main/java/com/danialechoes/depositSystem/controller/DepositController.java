package com.danialechoes.depositSystem.controller;

import com.danialechoes.depositSystem.dto.DepositDto;
import com.danialechoes.depositSystem.facade.DepositFacade;
import com.danialechoes.depositSystem.model.Currency;
import com.danialechoes.depositSystem.service.DepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    private static final Logger logger = LoggerFactory.getLogger(DepositController.class);
    private final DepositFacade depositFacade;

    @Autowired
    public DepositController(DepositFacade depositFacade) {
        this.depositFacade = depositFacade;
        logger.info("DepositController initialized");
    }

    @PostMapping("/customer/{customerId}/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPOSIT')")
    public DepositDto addDeposit(Long customerId, @RequestParam Currency currency) {
        logger.info("Adding new deposit for customerId: {}, currency: {}", customerId, currency);
        DepositDto depositDto = depositFacade.addDeposit(customerId, currency);
        logger.info("Successfully added deposit with ID: {} for customerId: {}", depositDto.getId(), customerId);
        return depositDto;
    }

    @PutMapping("/{id}/deposit")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPOSIT')")
    public DepositDto depositAmount(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return depositFacade.depositAmount(id, amount);
    }

    @PutMapping("/{id}/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPOSIT')")
    public DepositDto withdrawAmount(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return depositFacade.withdrawAmount(id, amount);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPOSIT')")
    public List<DepositDto> getDepositsByCustomerId(@PathVariable Long customerId) {
        return depositFacade.getDepositsByCustomerId(customerId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPOSIT')")
    public void deleteDeposit(@PathVariable Long id) {
        depositFacade.deleteDeposit(id);
    }

    @PutMapping("/{fromDepositId}/transfer/{toDepositId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEPOSIT')")
    public void transferAmount(@PathVariable Long fromDepositId, @PathVariable Long toDepositId,
                               @RequestParam BigDecimal amount) {
       depositFacade.transferAmount(fromDepositId, toDepositId, amount);
    }
}
