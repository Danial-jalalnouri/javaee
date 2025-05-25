package com.danialechoes.phonebook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class PersonalContact extends Contact {
    private String family;

    public PersonalContact() {
        this.setType(ContactType.PERSONAL);
    }
}
