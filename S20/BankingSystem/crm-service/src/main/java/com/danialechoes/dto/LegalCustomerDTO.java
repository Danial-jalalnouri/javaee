package com.danialechoes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LegalCustomerDTO extends CustomerDTO {
    @NotBlank(message = "Fax number is required")
    private String fax;
}
