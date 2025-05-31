package com.danialechoes.customerSystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class LegalCustomer extends Customer {
    private String fax;

    public LegalCustomer() {
        this.setType(CustomerType.LEGAL);
    }
}
