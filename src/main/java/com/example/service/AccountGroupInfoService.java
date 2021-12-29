package com.example.service;

import com.example.entity.AccountGroupInfo;
import com.example.entity.AccountRole;
import org.springframework.stereotype.Service;

import java.util.Set;

public interface AccountGroupInfoService {

    void create(Integer accountId, Integer groupId, AccountRole accountRole);

    AccountGroupInfo getAccountGroupInfo(Integer groupId, Integer accountId);

    Set<AccountGroupInfo> getAccountGroupInfosByAccountId(Integer accountId);

    Set<AccountGroupInfo> getAccountGroupInfosByGroupId(Integer groupId);

    void deleteAccountGroupInfo(AccountGroupInfo accountGroupInfo);
}