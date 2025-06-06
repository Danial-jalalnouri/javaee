package com.danialechoes.customerSystem.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "legal_customer")
@PrimaryKeyJoinColumn(name = "id")
@DiscriminatorValue("LEGAL")
public class LegalCustomer extends Customer {
    private String fax;

    public LegalCustomer() {
        this.setType(CustomerType.LEGAL);
    }
}
