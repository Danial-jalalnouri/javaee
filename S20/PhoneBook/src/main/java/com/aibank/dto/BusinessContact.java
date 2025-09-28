package com.aibank.dto;

public class BusinessContact extends Contact {
    private String fax;

    public BusinessContact(String name, String number, String fax) {
        super(name, number, ContactType.BUSINESS);
        this.fax = fax;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Override
    public String toString() {
        return super.toString() + ", Fax: " + fax;
    }
}
