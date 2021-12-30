package com.example.service;

import com.example.common.validator.AccountCreationValidator;
import com.example.entity.Account;
import com.example.entity.AccountGroupInfo;
import com.example.entity.AccountRole;
import com.example.exception.AccountCreationException;
import com.example.exception.AccountNotFoundException;
import com.example.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

@org.springframework.stereotype.Service
public class AccountService implements Service<Account> {

    private final Repository<Account> accountRepository;
    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupService groupService;
    private final AccountCreationValidator accountCreationValidator;

    @Autowired
    public AccountService(Repository<Account> accountRepository, GroupService groupService,
                          AccountGroupInfoService accountGroupInfoService, AccountCreationValidator accountCreationValidator) {
        this.accountRepository = accountRepository;
        this.groupService = groupService;
        this.accountGroupInfoService = accountGroupInfoService;
        this.accountCreationValidator = accountCreationValidator;
    }

    @Override
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
        System.out.println("The account with user's name " + account.getUser().getName() + " deleted!");
    }

    @Override
    public String toString() {
        return "AccountServiceImpl{" +
                "accounts size = " + accountRepository.getSize() +
                '}';
    }

    @Override
    public void save(Account account) {
        if (!accountCreationValidator.test(account)) {
            throw new AccountCreationException("Account not valid!");
        }

        accountRepository.save(account);
        System.out.println("The account with user's name " + account.getUser().getName() + " saved!");
    }

    @Override
    public Account get(Integer id) {
        if (id == null) {
            throw new NullPointerException("Account id is null!");
        }

        return accountRepository.get(id);
    }

    public Map<Integer, Account> getAll() {
        return accountRepository.getAll();
    }
}