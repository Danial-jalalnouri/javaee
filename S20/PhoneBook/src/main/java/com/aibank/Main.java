package com.aibank;

import com.aibank.dto.BusinessContact;
import com.aibank.dto.Contact;
import com.aibank.dto.PersonalContact;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Contact> contacts = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                case 1 -> addContact();
                case 2 -> deleteById();
                case 3 -> editById();
                case 4 -> printAllContacts();
                case 5 -> searchByName();
                case 6 -> searchByFamily();
                case 7 -> searchAndEditByName();
                case 8 -> searchAndDeleteByName();
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("0. Exit");
        System.out.println("1. Add Contact");
        System.out.println("2. Delete by id");
        System.out.println("3. Edit by id");
        System.out.println("4. Print All Contacts");
        System.out.println("5. Search and print contacts by name");
        System.out.println("6. Search and print contacts by family");
        System.out.println("7. Search and edit contacts by name");
        System.out.println("8. Search and delete contacts by name");
    }

    private static void addContact() {
        System.out.println("\nSelect contact type:");
        System.out.println("1. Personal Contact");
        System.out.println("2. Business Contact");

        int type = getIntInput("Enter your choice (1 or 2): ");

        String name = getStringInput("Enter name: ");
        String number = getStringInput("Enter number: ");

        switch (type) {
            case 1 -> {
                String family = getStringInput("Enter family name: ");
                contacts.add(new PersonalContact(name, number, family));
                System.out.println("Personal contact added successfully!");
            }
            case 2 -> {
                String fax = getStringInput("Enter fax number: ");
                contacts.add(new BusinessContact(name, number, fax));
                System.out.println("Business contact added successfully!");
            }
            default -> System.out.println("Invalid contact type!");
        }
    }

    private static void deleteById() {
        long id = getIntInput("Enter contact ID to delete: ");
        boolean removed = contacts.removeIf(contact -> contact.getId() == id);
        if (removed) {
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found!");
        }
    }

    private static void editById() {
        long id = getIntInput("Enter contact ID to edit: ");
        Contact contactToEdit = contacts.stream()
                .filter(contact -> contact.getId() == id)
                .findFirst()
                .orElse(null);

        if (contactToEdit == null) {
            System.out.println("Contact not found!");
            return;
        }

        String name = getStringInput("Enter new name (or press enter to skip): ");
        if (!name.isEmpty()) {
            contactToEdit.setName(name);
        }

        String number = getStringInput("Enter new number (or press enter to skip): ");
        if (!number.isEmpty()) {
            contactToEdit.setNumber(number);
        }

        if (contactToEdit instanceof PersonalContact personalContact) {
            String family = getStringInput("Enter new family name (or press enter to skip): ");
            if (!family.isEmpty()) {
                personalContact.setFamily(family);
            }
        } else if (contactToEdit instanceof BusinessContact businessContact) {
            String fax = getStringInput("Enter new fax number (or press enter to skip): ");
            if (!fax.isEmpty()) {
                businessContact.setFax(fax);
            }
        }

        System.out.println("Contact updated successfully!");
    }

    private static void printAllContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found!");
            return;
        }
        System.out.println("\nAll Contacts:");
        contacts.forEach(System.out::println);
    }

    private static void searchByName() {
        String searchName = getStringInput("Enter name to search: ");
        List<Contact> found = contacts.stream()
                .filter(contact -> contact.getName().toLowerCase().contains(searchName.toLowerCase()))
                .toList();
        printSearchResults(found);
    }

    private static void searchByFamily() {
        String searchFamily = getStringInput("Enter family name to search: ");
        List<Contact> found = contacts.stream()
                .filter(contact -> contact instanceof PersonalContact)
                .filter(contact -> ((PersonalContact) contact).getFamily().toLowerCase().contains(searchFamily.toLowerCase()))
                .toList();
        printSearchResults(found);
    }

    private static void searchAndEditByName() {
        String searchName = getStringInput("Enter name to search and edit: ");
        List<Contact> found = contacts.stream()
                .filter(contact -> contact.getName().toLowerCase().contains(searchName.toLowerCase()))
                .toList();

        if (found.isEmpty()) {
            System.out.println("No contacts found!");
            return;
        }

        System.out.println("\nFound contacts:");
        for (int i = 0; i < found.size(); i++) {
            System.out.println(i + 1 + ". " + found.get(i));
        }

        int choice = getIntInput("Enter the number of the contact to edit (0 to cancel): ");
        if (choice > 0 && choice <= found.size()) {
            Contact contactToEdit = found.get(choice - 1);
            editContact(contactToEdit);
        }
    }

    private static void searchAndDeleteByName() {
        String searchName = getStringInput("Enter name to search and delete: ");
        List<Contact> found = contacts.stream()
                .filter(contact -> contact.getName().toLowerCase().contains(searchName.toLowerCase()))
                .toList();

        if (found.isEmpty()) {
            System.out.println("No contacts found!");
            return;
        }

        System.out.println("\nFound contacts:");
        for (int i = 0; i < found.size(); i++) {
            System.out.println(i + 1 + ". " + found.get(i));
        }

        int choice = getIntInput("Enter the number of the contact to delete (0 to cancel): ");
        if (choice > 0 && choice <= found.size()) {
            contacts.remove(found.get(choice - 1));
            System.out.println("Contact deleted successfully!");
        }
    }

    private static void editContact(Contact contact) {
        String name = getStringInput("Enter new name (or press enter to skip): ");
        if (!name.isEmpty()) {
            contact.setName(name);
        }

        String number = getStringInput("Enter new number (or press enter to skip): ");
        if (!number.isEmpty()) {
            contact.setNumber(number);
        }

        if (contact instanceof PersonalContact personalContact) {
            String family = getStringInput("Enter new family name (or press enter to skip): ");
            if (!family.isEmpty()) {
                personalContact.setFamily(family);
            }
        } else if (contact instanceof BusinessContact businessContact) {
            String fax = getStringInput("Enter new fax number (or press enter to skip): ");
            if (!fax.isEmpty()) {
                businessContact.setFax(fax);
            }
        }

        System.out.println("Contact updated successfully!");
    }

    private static void printSearchResults(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found!");
            return;
        }
        System.out.println("\nFound contacts:");
        contacts.forEach(System.out::println);
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
