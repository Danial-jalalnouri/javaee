package com.danialechoes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionDTO {
    @NotBlank
    private String accountNumber;

    @NotNull
    @Positive
    private BigDecimal amount;
}
