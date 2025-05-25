package com.danialechoes.phonebook.mapper;

import com.danialechoes.phonebook.dto.BusinessContactDto;
import com.danialechoes.phonebook.dto.ContactDto;
import com.danialechoes.phonebook.dto.PersonalContactDto;
import com.danialechoes.phonebook.model.BusinessContact;
import com.danialechoes.phonebook.model.Contact;
import com.danialechoes.phonebook.model.PersonalContact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    PersonalContact toEntity(PersonalContactDto dto);
    PersonalContactDto toDto(PersonalContact entity);

    BusinessContact toEntity(BusinessContactDto dto);
    BusinessContactDto toDto(BusinessContact entity);

    default Contact toEntity(Object dto) {
        if (dto instanceof PersonalContactDto) {
            return toEntity((PersonalContactDto) dto);
        } else if (dto instanceof BusinessContactDto) {
            return toEntity((BusinessContactDto) dto);
        }
        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass());
    }

    default ContactDto toDto(Contact entity) {
        if (entity instanceof PersonalContact) {
            return toDto((PersonalContact) entity);
        } else if (entity instanceof BusinessContact) {
            return toDto((BusinessContact) entity);
        }
        throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass());
    }
}
