package com.aibank.dto;

public abstract class Contact {
    private static long idCounter = 1;
    private final long id;
    private String name;
    private String number;
    private ContactType contactType;

    public Contact(String name, String number, ContactType contactType) {
        this.id = idCounter++;
        this.name = name;
        this.number = number;
        this.contactType = contactType;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ContactType getContactType() {
        return contactType;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Number: " + number + ", Type: " + contactType;
    }
}
