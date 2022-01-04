package com.example.service;

import com.example.common.validator.AccountCreationValidator;
import com.example.entity.Account;
import com.example.entity.AccountGroupInfo;
import com.example.entity.AccountRole;
import com.example.entity.Contact;
import com.example.exception.AccountCreationException;
import com.example.exception.AccountNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Service
@Validated
public class AccountService {

    private final AccountRepository accountRepository;
    private final ContactRepository contactRepository;
    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupService groupService;
    private final AccountCreationValidator accountCreationValidator;

    @Autowired
    public AccountService(AccountRepository accountRepository, ContactRepository contactRepository, GroupService groupService,
                          AccountGroupInfoService accountGroupInfoService, AccountCreationValidator accountCreationValidator) {
        this.accountRepository = accountRepository;
        this.contactRepository = contactRepository;
        this.groupService = groupService;
        this.accountGroupInfoService = accountGroupInfoService;
        this.accountCreationValidator = accountCreationValidator;
    }

    public void delete(Integer id) {
        Account account = get(id);
        if (account == null) {
            throw new AccountNotFoundException("The account with id = " + id + " does not exist!");
        }

        Set<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(id);
        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
            if (accountGroupInfo.getAccountRole() == AccountRole.ADMIN) {
                groupService.delete(accountGroupInfo.getGroupId());
            } else {
                groupService.leaveGroup(accountGroupInfo.getGroupId(), id);
            }
        }

        accountRepository.delete(id);
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

        return accountRepository.get(id);
    }

    public List<Account> getAll() {
        return accountRepository.getAll();
    }

    public void update(int id, Account account) {
        //accountRepository.update(id, account);
    }

    public Account getByTelephoneNumberAndPassword(String telephoneNumber, String encodedPassword) {
        return accountRepository.getByNameAndPassword(telephoneNumber, encodedPassword);
    }
}