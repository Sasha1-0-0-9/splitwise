package com.example.service;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.exception.ContactCreationException;
import com.example.repository.AccountContactRepository;
import com.example.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Service
@Validated
public class ContactService {

    private final ContactRepository contactRepository;
    private final AccountContactRepository accountContactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository, AccountContactRepository accountContactRepository) {
        this.contactRepository = contactRepository;
        this.accountContactRepository = accountContactRepository;
    }

    public Contact get(String telephoneNumber) {
        return contactRepository.get(telephoneNumber);
    }

    public List<Contact> getByAccountId(Integer accountId) {
        return contactRepository.getByAccountId(accountId);
    }

    public void save(@Valid Contact contact) {
        if (get(contact.getPhoneNumber()) != null) {
            throw new ContactCreationException("An account with the same phone number already exists!");
        }

        contactRepository.save(contact);
    }

    public void add(Account account,
                    @NotEmpty(message = "Telephone number should not be empty")
                    @Pattern(regexp = "(^$|[0-9]{10})") String phoneNumber) {
        if (account.getTelephoneNumber().equals(phoneNumber)) {
            throw new RuntimeException("This phone number belongs to you!");
        }

        Contact contact = getByAccountId(account.getId()).stream()
                .filter(s -> s.getPhoneNumber().equals(phoneNumber))
                .findAny().orElse(null);

        if (contact != null) {
            throw new RuntimeException("You already have a contact with this number!");
        }

        accountContactRepository.save(account.getId(), phoneNumber);
    }
}

