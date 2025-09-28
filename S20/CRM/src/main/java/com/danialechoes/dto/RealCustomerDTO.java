package com.danialechoes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RealCustomerDTO extends CustomerDTO {
    @NotBlank(message = "Family name is required")
    private String family;
}
