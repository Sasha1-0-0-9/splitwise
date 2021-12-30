package com.example.service;

import com.example.entity.AccountGroupInfo;
import com.example.entity.AccountRole;

public class DeleteGroupService {

    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupService groupService;

    public DeleteGroupService(AccountGroupInfoService accountGroupInfoService, GroupService groupService) {
        this.accountGroupInfoService = accountGroupInfoService;
        this.groupService = groupService;
    }

    public void delete(Integer groupId, Integer accountId) {
        if (accountId == null || groupId == null) {
            throw new NullPointerException("The group id or account id is null!");
        }

        AccountGroupInfo accountGroupInfo = accountGroupInfoService.getAccountGroupInfo(groupId, accountId);
        if ((accountGroupInfo != null) && (accountGroupInfo.getAccountRole() != AccountRole.ADMIN)) {
            System.out.println("The group cannot be deleted!");
            return;
        }

        groupService.delete(groupId);
    }
}
