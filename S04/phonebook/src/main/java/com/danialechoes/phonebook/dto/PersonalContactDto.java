package com.danialechoes.phonebook.dto;

import com.danialechoes.phonebook.model.ContactType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class PersonalContactDto extends ContactDto {
    private String family;

    public PersonalContactDto() {
        this.setType(ContactType.PERSONAL);
    }
}
