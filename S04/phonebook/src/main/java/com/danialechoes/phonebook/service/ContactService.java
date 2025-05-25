package com.danialechoes.phonebook.service;

import com.danialechoes.phonebook.dao.ContactDao;
import com.danialechoes.phonebook.model.Contact;
import com.danialechoes.phonebook.model.ContactType;
import com.danialechoes.phonebook.model.PersonalContact;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ContactService {

    private final ContactDao contactDao;

    @Autowired
    public ContactService(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @PostConstruct
    public void init() {
        // Initialize some sample contacts
        addContact(PersonalContact.builder()
                .id(null)
                .name("Alice")
                .phoneNumber("5551234567")
                .type(ContactType.PERSONAL)
                .family("Johnson")
                .build());
    }

    public Contact addContact(Contact contact) {
        return contactDao.save(contact);
    }

    public Contact updateContact(Long id, Contact updatedContact) {
        if (contactDao.existsById(id)) {
            return contactDao.update(id, updatedContact);
        }
        return null; // or throw an exception
    }

    public boolean deleteContact(Long id) {
        if (contactDao.existsById(id)) {
            contactDao.delete(id);
            return true;
        }
        return false; // or throw an exception
    }

    public Contact getContactById(Long id) {
        return contactDao.findById(id);
    }

    public List<Contact> getAllContacts() {
        return contactDao.findAll();
    }
}
