package com.danialechoes.phonebook.service;

import com.danialechoes.phonebook.model.Contact;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ContactService {
    // This is a simple service class that handles business logic
    // For now, it doesn't have any methods or properties
    // You can add methods to handle contact-related operations
    // such as adding, updating, deleting, and retrieving contacts

    private final Map<Long, Contact> contacts = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // Initialize some sample contacts
        addContact(new Contact(null, "John Doe", "123456789"));
        addContact(new Contact(null, "Jane Smith", "987654321"));
    }

    public Contact addContact(Contact contact) {
        long id = currentId.incrementAndGet();
        contact.setId(id);
        contacts.put(id, contact);
        return contact;
    }

    public Contact updateContact(Long id, Contact updatedContact) {
        if (contacts.containsKey(id)) {
            updatedContact.setId(id);
            contacts.put(id, updatedContact);
            return updatedContact;
        }
        return null; // or throw an exception
    }

    public boolean deleteContact(Long id) {
        if (contacts.containsKey(id)) {
            contacts.remove(id);
            return true;
        }
        return false; // or throw an exception
    }

    public Contact getContactById(Long id) {
        return contacts.get(id);
    }

    public List<Contact> getAllContacts() {
        return contacts.values().stream().toList();
    }
}
