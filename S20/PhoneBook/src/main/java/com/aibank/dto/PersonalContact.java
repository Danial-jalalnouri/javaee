package com.aibank.dto;

public class PersonalContact extends Contact {
    private String family;

    public PersonalContact(String name, String number, String family) {
        super(name, number, ContactType.PERSONAL);
        this.family = family;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    @Override
    public String toString() {
        return super.toString() + ", Family: " + family;
    }
}
