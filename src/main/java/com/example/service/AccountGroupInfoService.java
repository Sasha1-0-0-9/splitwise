package com.example.service;

import com.example.entity.AccountGroupInfo;
import com.example.exception.AccountGroupInfoCreationException;
import com.example.repository.AccountGroupInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountGroupInfoService {

    private final AccountGroupInfoRepo accountGroupInfoRepository;

    @Autowired
    public AccountGroupInfoService(AccountGroupInfoRepo accountGroupInfoRepository) {
        this.accountGroupInfoRepository = accountGroupInfoRepository;
    }

    public void create(Integer accountId, Integer groupId) {
        if (accountId == null || groupId == null) {
            throw new AccountGroupInfoCreationException("AccountGroupInfo did not created!");
        }

        AccountGroupInfo accountGroupInfo = new AccountGroupInfo(accountId, groupId);
        accountGroupInfo.setId(accountGroupInfo.getId());
        accountGroupInfoRepository.save(accountGroupInfo);
        System.out.println("The account with id " + accountGroupInfo.getAccountId() + " added to the group "
                + accountGroupInfo.getGroupId()  + "!");
    }

    public AccountGroupInfo getAccountGroupInfo(Integer groupId, Integer accountId) {
        if (accountId == null || groupId == null) {
            throw new NullPointerException("The group id or account id is null!");
        }

        return accountGroupInfoRepository.getByAccountAndGroupId(accountId, groupId);
    }

    public List<AccountGroupInfo> getAccountGroupInfosByAccountId(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException("The account id is null!");
        }

        return accountGroupInfoRepository.getByAccountId(accountId);
    }

    public List<AccountGroupInfo> getAccountGroupInfosByGroupId(Integer groupId) {
        if (groupId == null) {
            throw new NullPointerException("The group id is null!");
        }

        return accountGroupInfoRepository.getByGroupId(groupId);
    }

    public void deleteAccountGroupInfo(AccountGroupInfo accountGroupInfo) {
        accountGroupInfoRepository.delete(accountGroupInfo);
        System.out.println("The account with id " + accountGroupInfo.getAccountId() + " deleted from the group "
                + accountGroupInfo.getGroupId() + "!");
    }
}