package com.danialechoes.phonebook;

import com.danialechoes.phonebook.model.Contact;
import com.danialechoes.phonebook.service.ContactService;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ContactServiceTest {

    @Test
    public void testRaceConditionWithLong() throws InterruptedException {
        ContactService contactService = new ContactService();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        int numThreads = 100;
        CountDownLatch latch = new CountDownLatch(numThreads);
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                contactService.addContact(new Contact(null, "John Doe", "123456789"));
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        List<Contact> allContacts = contactService.getAllContacts();
        Set<Long> ids = new HashSet<>();
        for (Contact contact : allContacts) {
            if (!ids.add(contact.getId())) {
                System.out.println("Duplicate ID found: " + contact.getId());
            }
        }

        assertEquals(allContacts.size(), ids.size(), "Duplicate IDs found");
    }

}
