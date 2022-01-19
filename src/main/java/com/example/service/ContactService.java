package com.example.service;

import com.example.entity.Contact;
import com.example.exception.ContactCreationException;
import com.example.repository.ContactRepo;
import com.example.repository.remove.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class ContactService {

    private final ContactRepo contactRepository;

    @Autowired
    public ContactService(ContactRepo contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact get(String telephoneNumber) {
        return contactRepository.getById(telephoneNumber);
    }

    public List<Contact> getByAccountId(Integer accountId) {
        return contactRepository.getByAccountId(accountId);
    }

    public void save(@Valid Contact contact) {
       /* if (get(contact.getTelephoneNumber()) != null) {
            throw new ContactCreationException("An account with the same phone number already exists!");
        }*/

        contactRepository.save(contact);
    }
}
