package com.danialechoes.phonebook.facade;

import com.danialechoes.phonebook.dto.ContactDto;
import com.danialechoes.phonebook.mapper.ContactMapper;
import com.danialechoes.phonebook.model.Contact;
import com.danialechoes.phonebook.model.ContactType;
import com.danialechoes.phonebook.model.PersonalContact;
import com.danialechoes.phonebook.service.ContactService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ContactFacade {

    private final ContactMapper mapper;
    private final ContactService contactService;

    @Autowired
    public ContactFacade(ContactMapper mapper, ContactService contactService) {
        this.mapper = mapper;
        this.contactService = contactService;
    }

    public ContactDto addContact(ContactDto contact) {
        Contact entity = mapper.toEntity(contact);
        entity = contactService.addContact(entity);
        return mapper.toDto(entity);
    }

    public ContactDto updateContact(Long id, ContactDto updatedContact) {
        Contact entity = mapper.toEntity(updatedContact);
        entity = contactService.updateContact(id, entity);
        return entity != null ? mapper.toDto(entity) : null;
    }

    public boolean deleteContact(Long id) {
        return contactService.deleteContact(id);
    }

    public ContactDto getContactById(Long id) {
        Contact contactById = contactService.getContactById(id);
        return contactById != null ? mapper.toDto(contactById) : null;
    }

    public List<ContactDto> getAllContacts() {
        return contactService.getAllContacts()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
