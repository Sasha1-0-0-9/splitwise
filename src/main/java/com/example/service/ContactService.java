package com.example.service;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact get(String telephoneNumber) {
        return contactRepository.get(telephoneNumber);
    }

    public List<Contact> getByAccountId(Integer accountId) {
        return contactRepository.getByAccountId(accountId);
    }

    public Account getAccount(String telephoneNumber) {
        return contactRepository.getAccount(telephoneNumber);
    }
}
