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
public class BusinessContactDto extends ContactDto {
    private String fax;

    public BusinessContactDto() {
        this.setType(ContactType.BUSINESS);
    }
}
