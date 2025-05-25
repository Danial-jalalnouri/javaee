package com.danialechoes.phonebook.controller;

import com.danialechoes.phonebook.dto.BusinessContactDto;
import com.danialechoes.phonebook.dto.ContactDto;
import com.danialechoes.phonebook.dto.PersonalContactDto;
import com.danialechoes.phonebook.facade.ContactFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactFacade contactFacade;

    // This is a simple controller class that handles HTTP requests

    @Operation(summary = "Get all contacts", description = "Retrieve a list of all contacts")
    @GetMapping
    public List<ContactDto> getAllContacts() {
        return contactFacade.getAllContacts();
    }

    @Operation(summary = "Get contact by ID", description = "Retrieve a contact by its unique identifier")
    @GetMapping("/{id}")
    public ContactDto getContactById(@PathVariable Long id) {
        return contactFacade.getContactById(id);
    }

    @Operation(summary = "Add a new contact", description = "Create a new contact in the phonebook")
    @PostMapping
    public ContactDto addContact(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Contact object to be added",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    oneOf = {
                                            PersonalContactDto.class,
                                            BusinessContactDto.class
                                    }
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Personal Contact Example",
                                            value = "{"
                                                + "\"name\": \"John Doe\","
                                                + "\"phoneNumber\": \"+1234567890\","
                                                + "\"type\": \"PERSONAL\","
                                                + "\"family\": \"Doe\""
                                                + "}"
                                    ),
                                    @ExampleObject(
                                            name = "Business Contact Example",
                                            value = "{"
                                                + "\"name\": \"John Doe\","
                                                + "\"phoneNumber\": \"+1234567890\","
                                                + "\"type\": \"BUSINESS\","
                                                + "\"fax\": \"+0987654321\""
                                                + "}"
                                    )
                            }
                    )
            )
            @RequestBody ContactDto contact
    ) {
        return contactFacade.addContact(contact);
    }

    @Operation(summary = "Update an existing contact", description = "Update the details of an existing contact")
    @PutMapping("/{id}")
    public ContactDto updateContact(@PathVariable Long id,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                         description = "Updated contact object",
                                         required = true,
                                         content = @Content(
                                                 mediaType = "application/json",
                                                 schema = @Schema(
                                                         oneOf = {
                                                                 PersonalContactDto.class,
                                                                 BusinessContactDto.class
                                                         }
                                                 ),
                                                 examples = {
                                                         @ExampleObject(
                                                                 name = "Personal Contact Example",
                                                                 value = "{"
                                                                         + "\"name\": \"John Doe\","
                                                                         + "\"phoneNumber\": \"+1234567890\","
                                                                         + "\"type\": \"PERSONAL\","
                                                                         + "\"family\": \"Doe\""
                                                                         + "}"
                                                         ),
                                                         @ExampleObject(
                                                                 name = "Business Contact Example",
                                                                 value = "{"
                                                                         + "\"name\": \"John Doe\","
                                                                         + "\"phoneNumber\": \"+1234567890\","
                                                                         + "\"type\": \"BUSINESS\","
                                                                         + "\"fax\": \"+0987654321\""
                                                                         + "}"
                                                         )
                                                 }
                                         )
                                 )
                                 @RequestBody ContactDto updatedContact
    ) {
        return contactFacade.updateContact(id, updatedContact);
    }

    @Operation(summary = "Delete a contact", description = "Remove a contact from the phonebook by its ID")
    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable Long id) {
        boolean deleted = contactFacade.deleteContact(id);
        if (deleted) {
            return "Contact deleted successfully";
        } else {
            return "Contact not found";
        }
    }

}
