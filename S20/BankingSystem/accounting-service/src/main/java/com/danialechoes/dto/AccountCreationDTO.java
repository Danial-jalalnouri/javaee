package com.danialechoes.dto;

import com.danialechoes.entity.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountCreationDTO {
    @NotNull
    private Long customerId;

    @NotNull
    private Currency currency;

    @NotNull
    @Positive
    private BigDecimal initialBalance;
}
