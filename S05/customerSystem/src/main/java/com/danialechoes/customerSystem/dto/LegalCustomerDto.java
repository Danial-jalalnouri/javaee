package com.danialechoes.customerSystem.dto;

import com.danialechoes.customerSystem.model.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class LegalCustomerDto extends CustomerDto {
    private String fax;

    public LegalCustomerDto() {
        this.setType(CustomerType.LEGAL);
    }
}
