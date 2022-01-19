package com.example.service;

import com.example.common.validator.AccountCreationValidator;
import com.example.entity.Account;
import com.example.entity.AccountGroupInfo;
import com.example.exception.AccountCreationException;
import com.example.exception.AccountNotFoundException;

import com.example.repository.remove.ContactRepository;
import com.example.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class AccountService {

    private final AccountRepo accountRepository;
    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupService groupService;
    private final AccountCreationValidator accountCreationValidator;

    @Autowired
    public AccountService(AccountRepo accountRepository, GroupService groupService,
                          AccountGroupInfoService accountGroupInfoService, AccountCreationValidator accountCreationValidator) {
        this.accountRepository = accountRepository;
        this.groupService = groupService;
        this.accountGroupInfoService = accountGroupInfoService;
        this.accountCreationValidator = accountCreationValidator;
    }

    public void delete(Integer id) {
        Account account = get(id);
        if (account == null) {
            throw new AccountNotFoundException("The account with id = " + id + " does not exist!");
        }

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(id);
        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
                groupService.leaveGroup(accountGroupInfo.getGroupId(), id);
        }

        accountRepository.getById(id);
    }

    public void save(@Valid Account account) {
        if (!accountCreationValidator.test(account)) {
            throw new AccountCreationException("Account not valid!");
        }

        accountRepository.save(account);
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

    public void update(int id, String email) {
        accountRepository.update(email, id);
    }

    public Account getByTelephoneNumber(String telephoneNumber) {
        Account account = accountRepository.getByTelephoneNumber(telephoneNumber);
        if (account == null) {
            throw new AccountNotFoundException("The account with phone number = " + telephoneNumber + " does not exist!");
        }

        return account;
    }

    public Account getByEmail(String email) {
        Account account = accountRepository.getAccByName(email);
        if (account == null) {
            throw new AccountNotFoundException("The account with email = " + email + " does not exist!");
        }

        return account;
    }
}