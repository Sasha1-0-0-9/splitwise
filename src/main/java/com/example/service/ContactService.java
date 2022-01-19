package com.example.service;

import com.example.entity.Contact;
import com.example.exception.ContactCreationException;
import com.example.repository.ContactRepo;
import com.example.repository.remove.AccountContactRepository;
import com.example.repository.remove.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class ContactService {

    private final ContactRepository contactRepository1;
    private final ContactRepo contactRepository;
    private final AccountContactRepository accountContactRepository;

    @Autowired
    public ContactService(ContactRepo contactRepository, AccountContactRepository accountContactRepository, ContactRepository contactRepository1) {
        this.contactRepository = contactRepository;
        this.contactRepository1 = contactRepository1;
        this.accountContactRepository = accountContactRepository;
    }

    public Contact get(String telephoneNumber) {
        return contactRepository.findContactByPhoneNumber(telephoneNumber);
    }

    public List<Contact> getByAccountId(Integer accountId) {
        return contactRepository1.getByAccountId(accountId);
    }

    public void save(@Valid Contact contact) {
        if (get(contact.getPhoneNumber()) != null) {
            throw new ContactCreationException("An account with the same phone number already exists!");
        }

        contactRepository.save(contact);
    }
}
