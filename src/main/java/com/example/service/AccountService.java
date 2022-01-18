package com.example.service;

import com.example.common.validator.AccountCreationValidator;
import com.example.entity.Account;
import com.example.entity.AccountGroupInfo;
import com.example.exception.AccountCreationException;
import com.example.exception.AccountNotFoundException;

import com.example.repository.remove.ContactRepository;
import com.example.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class AccountService {

    private final AccountRepo accountRepository;
    private final ContactRepository contactRepository;
    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupService groupService;
    private final AccountCreationValidator accountCreationValidator;

    @Autowired
    public AccountService(AccountRepo accountRepository, ContactRepository contactRepository, GroupService groupService,
                          AccountGroupInfoService accountGroupInfoService, AccountCreationValidator accountCreationValidator) {
        this.accountRepository = accountRepository;
        this.groupService = groupService;
        this.accountGroupInfoService = accountGroupInfoService;
        this.accountCreationValidator = accountCreationValidator;
        this.contactRepository = contactRepository;
    }

    public void delete(Integer id) {
        Account account = get(id);
        if (account == null) {
            throw new AccountNotFoundException("The account with id = " + id + " does not exist!");
        }

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(id);
        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
            //if (accountGroupInfo.getAccountRole() == AccountRole.ADMIN) {
              //  groupService.delete(accountGroupInfo.getGroupId());
            //} else {
                groupService.leaveGroup(accountGroupInfo.getGroupId(), id);
            //}
        }

        accountRepository.getById(id);
    }

    public Account save(@Valid Account account) {
        if (!accountCreationValidator.test(account)) {
            throw new AccountCreationException("Account not valid!");
        }

        return accountRepository.save(account);
    }

    public Account get(Integer id) {
        if (id == null) {
            throw new NullPointerException("Account id is null!");
        }

        return accountRepository.getById(id);
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public void update(int id, Account account) {
        accountRepository.save(account);
    }

    /*public Account getByTelephoneNumberAndPassword(String telephoneNumber, String encodedPassword) {
        return accountRepository.getByNameAndPassword(telephoneNumber, encodedPassword);
    }*/

    public Account getByTelephoneNumber(String telephoneNumber) {
        Account account = accountRepository.getByTelephoneNumber(telephoneNumber);
        if (account == null) {
            throw new AccountNotFoundException("The account with phone number = " + telephoneNumber + " does not exist!");
        }

        return account;
    }
}