package com.danialechoes.phonebook.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Contact entity representing a phonebook contact")
public class Contact {
    @Schema(description = "Unique identifier for the contact", example = "1")
    private Long id;
    @Schema(description = "Name of the contact", example = "John Doe")
    private String name;
    @Schema(description = "Phone number of the contact", example = "+1234567890")
    private String phoneNumber;

    public Contact() {
    }

    public Contact(Long id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
