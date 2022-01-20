package com.example.service;

import com.example.entity.AccountGroupInfo;
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
        AccountGroupInfo accountGroupInfo = new AccountGroupInfo(accountId, groupId);
        accountGroupInfo.setId(accountGroupInfo.getId());
        accountGroupInfoRepository.save(accountGroupInfo);
    }

    public AccountGroupInfo getAccountGroupInfo(Integer groupId, Integer accountId) {
        return accountGroupInfoRepository.findAccountGroupInfoByAccountIdAndGroupId(accountId, groupId);
    }

    public List<AccountGroupInfo> getAccountGroupInfosByAccountId(Integer accountId) {
        return accountGroupInfoRepository.findAccountGroupInfosByAccountId(accountId);
    }

    public List<AccountGroupInfo> getAccountGroupInfosByGroupId(Integer groupId) {
        return accountGroupInfoRepository.findAccountGroupInfosByGroupId(groupId);
    }

    public void deleteAccountGroupInfo(AccountGroupInfo accountGroupInfo) {
        accountGroupInfoRepository.delete(accountGroupInfo);
    }
}