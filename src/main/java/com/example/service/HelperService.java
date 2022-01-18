package com.example.service;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HelperService {

    private final GroupService groupService;
    private final AccountService accountService;
    private final ContactService contactService;

    @Autowired
    public HelperService(GroupService groupService, AccountService accountService, ContactService contactService) {
        this.groupService = groupService;
        this.accountService = accountService;
        this.contactService = contactService;
    }

    public List<Contact> getContacts(Integer id) {
        List<Integer> idList = groupService.getIdAccounts(id);
        List<Account> accounts = idList.stream()
                .map(accountService::get)
                .collect(Collectors.toList());
        return accounts.stream()
                .map(s -> contactService.get(s.getTelephoneNumber()))
                .collect(Collectors.toList());
    }

    public String temp(Integer accountId, Integer id, String contact) {
        List<Contact> groupContacts = getContacts(id);

        Contact value = groupContacts.stream()
                .filter(p -> p.getTelephoneNumber().equals(contact))
                .findAny().orElse(null);

        if (value != null) {
            return "An account with this number is already in the group";
        } else {
            try {
                Account account = accountService.getByTelephoneNumber(contact);
                groupService.addToGroup(id, accountId, account.getId());
                return null;
            } catch (AccountNotFoundException e) {
                return e.getMessage();
            }
        }
    }
}
