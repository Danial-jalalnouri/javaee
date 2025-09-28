package com.danialechoes.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RealCustomer extends Customer {
    private String family;

    public RealCustomer() {
        setCustomerType(CustomerType.REAL);
    }
}
