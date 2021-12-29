package com.example.service;

import com.example.entity.AccountGroupInfo;
import com.example.entity.AccountRole;
import com.example.exception.AccountGroupInfoCreationException;
import com.example.repository.AccountGroupInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AccountGroupInfoServiceImpl implements AccountGroupInfoService {

    private final AccountGroupInfoRepository accountGroupInfoRepository;

    public AccountGroupInfoServiceImpl(AccountGroupInfoRepository accountGroupInfoRepository) {
        this.accountGroupInfoRepository = accountGroupInfoRepository;
    }

    @Override
    public void create(Integer accountId, Integer groupId, AccountRole accountRole) {
        if (accountId == null || groupId == null || accountRole == null) {
            throw new AccountGroupInfoCreationException("AccountGroupInfo did not created!");
        }

        AccountGroupInfo accountGroupInfo = new AccountGroupInfo(accountId, groupId, accountRole);
        accountGroupInfoRepository.save(accountGroupInfo);
        System.out.println("The account with id " + accountGroupInfo.getAccountId() + " added to the group "
                + accountGroupInfo.getGroupId() + " with the role " + accountGroupInfo.getAccountRole() + "!");
    }

    @Override
    public AccountGroupInfo getAccountGroupInfo(Integer groupId, Integer accountId) {
        if (accountId == null || groupId == null) {
            throw new NullPointerException("The group id or account id is null!");
        }

        return accountGroupInfoRepository.get(accountId, groupId);
    }

    @Override
    public Set<AccountGroupInfo> getAccountGroupInfosByAccountId(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException("The account id is null!");
        }

        return accountGroupInfoRepository.get(accountId, true);
    }

    @Override
    public Set<AccountGroupInfo> getAccountGroupInfosByGroupId(Integer groupId) {
        if (groupId == null) {
            throw new NullPointerException("The group id is null!");
        }

        return accountGroupInfoRepository.get(groupId, false);
    }

    @Override
    public void deleteAccountGroupInfo(AccountGroupInfo accountGroupInfo) {
        accountGroupInfoRepository.delete(accountGroupInfo);
        System.out.println("The account with id " + accountGroupInfo.getAccountId() + " deleted from the group "
                + accountGroupInfo.getGroupId() + " with role " + accountGroupInfo.getAccountRole() + "!");
    }

    @Override
    public String toString() {
        return "AccountGroupInfoServiceImpl{" +
                "accountGroupInfos size = " + accountGroupInfoRepository.getSize() +
                '}';
    }
}