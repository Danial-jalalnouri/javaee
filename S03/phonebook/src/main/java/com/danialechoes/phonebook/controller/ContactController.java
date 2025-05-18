package com.danialechoes.phonebook.controller;

import com.danialechoes.phonebook.model.Contact;
import com.danialechoes.phonebook.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // This is a simple controller class that handles HTTP requests

    @Operation(summary = "Get all contacts", description = "Retrieve a list of all contacts")
    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @PostMapping
    public Contact addContact(@RequestBody Contact contact) {
        return contactService.addContact(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        return contactService.updateContact(id, updatedContact);
    }

    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable Long id) {
        boolean deleted = contactService.deleteContact(id);
        if (deleted) {
            return "Contact deleted successfully";
        } else {
            return "Contact not found";
        }
    }

}
