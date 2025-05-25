package com.danialechoes.phonebook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class BusinessContact extends Contact {
    private String fax;

    public BusinessContact() {
        this.setType(ContactType.BUSINESS);
    }
}
