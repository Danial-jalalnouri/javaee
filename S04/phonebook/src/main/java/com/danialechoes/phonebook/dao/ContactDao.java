package com.danialechoes.phonebook.dao;

import com.danialechoes.phonebook.model.Contact;
import com.danialechoes.phonebook.model.ContactType;
import com.danialechoes.phonebook.model.PersonalContact;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ContactDao {

    private final Map<Long, Contact> contacts = new ConcurrentHashMap<>();
    private final AtomicLong currentId = new AtomicLong(0);

    @PostConstruct
    public void init() {
    }

    public Contact save(Contact contact) {
        long id = currentId.incrementAndGet();
        contact.setId(id);
        contacts.put(id, contact);
        return contact;
    }

    public Contact update(Long id, Contact contact) {
        contact.setId(id);
        contacts.put(id, contact);
        return contact;
    }

    public boolean delete(Long id) {
        return contacts.remove(id) != null;
    }

    public Contact findById(Long id) {
        return contacts.get(id);
    }

    public List<Contact> findAll() {
        return contacts.values().stream().toList();
    }

    public boolean existsById(Long id) {
        return contacts.containsKey(id);
    }
}
