package com.danialechoes.dto;

import com.danialechoes.entity.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class CustomerDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Number is required")
    private String number;

    @NotNull(message = "Customer type is required")
    private CustomerType customerType;
}
