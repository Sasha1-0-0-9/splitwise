package com.example.service;

import com.example.entity.Account;
import com.example.entity.AccountGroupInfo;
import com.example.entity.Contact;
import com.example.exception.AccountNotFoundException;

import com.example.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@Service
@Validated
public class AccountService {

    private final AccountRepo accountRepository;
    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupService groupService;
    private final ContactService contactService;

    @Autowired
    public AccountService(AccountRepo accountRepository, GroupService groupService,
                          AccountGroupInfoService accountGroupInfoService, ContactService contactService) {
        this.accountRepository = accountRepository;
        this.groupService = groupService;
        this.accountGroupInfoService = accountGroupInfoService;
        this.contactService = contactService;
    }

    public void delete(String email) {
        Account account = getByEmail(email);
        if (account == null) {
            throw new AccountNotFoundException("The account with email = " + email + " does not exist!");
        }

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(account.getId());
        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
            groupService.leaveGroup(accountGroupInfo.getGroupId(), account.getId());
        }

        accountRepository.delete(account);
    }

    public void save(@Valid Account account, String name) {
        Contact contact = new Contact(name, account.getTelephoneNumber());
        contactService.save(contact);
        accountRepository.save(account);
    }

    public Account get(Integer id) {
        if (id == null) {
            throw new NullPointerException("Account id is null!");
        }

        return accountRepository.getById(id);
    }

    public void update(String email, @Email String emailNew) {
        Account account = getByEmail(email);
        accountRepository.update(emailNew, account.getId());
    }

    public Account getByTelephoneNumber(String telephoneNumber) {
        Account account = accountRepository.findAccountByTelephoneNumber(telephoneNumber);
        if (account == null) {
            throw new AccountNotFoundException("The account with phone number = " + telephoneNumber + " does not exist!");
        }

        return account;
    }

    public Account getByEmail(String email) {
        Account account = accountRepository.findAccountByEmail(email);
        if (account == null) {
            throw new AccountNotFoundException("The account with email = " + email + " does not exist!");
        }

        return account;
    }
}