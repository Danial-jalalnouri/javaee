package com.danialechoes.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LegalCustomer extends Customer {
    private String fax;

    public LegalCustomer() {
        setCustomerType(CustomerType.LEGAL);
    }
}
