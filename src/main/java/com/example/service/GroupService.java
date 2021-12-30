package com.example.service;

import com.example.common.validator.GroupCreationValidator;
import com.example.entity.AccountGroupInfo;
import com.example.entity.AccountRole;
import com.example.entity.Group;
import com.example.exception.GroupCreationException;
import com.example.exception.GroupNotFoundException;
import com.example.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@org.springframework.stereotype.Service
public class GroupService implements Service<Group> {

    private final AccountGroupInfoService accountGroupInfoService;
    private final Repository<Group> groupRepository;
    private final GroupCreationValidator groupCreationValidator;

    @Autowired
    public GroupService(AccountGroupInfoService accountGroupInfoService, Repository<Group> groupRepository, GroupCreationValidator groupCreationValidator) {
        this.accountGroupInfoService = accountGroupInfoService;
        this.groupRepository = groupRepository;
        this.groupCreationValidator = groupCreationValidator;
    }

    public void addToGroup(Integer groupId, Integer accountId, Integer addedBy) {
        Group group = get(groupId);
        if (group == null) {
            throw new GroupNotFoundException("The group with id = " + groupId + " does not exist!");
        }

        AccountGroupInfo accountGroupInfo = accountGroupInfoService.getAccountGroupInfo(groupId, accountId);
        if ((accountGroupInfo != null) && (accountGroupInfo.getAccountRole() == AccountRole.ADMIN)) {
            accountGroupInfoService.create(addedBy, groupId, AccountRole.USER);
        }
    }

    public void leaveGroup(Integer groupId, Integer accountId) {
        if (accountId == null || groupId == null) {
            throw new NullPointerException("The group id or account id is null!");
        }

        AccountGroupInfo accountGroupInfo = accountGroupInfoService.getAccountGroupInfo(groupId, accountId);
        if (accountGroupInfo != null) {
            accountGroupInfoService.deleteAccountGroupInfo(accountGroupInfo);
        }
    }

    @Override
    public String toString() {
        return "GroupServiceImpl{" +
                "groups size = " + groupRepository.getSize() +
                '}';
    }

    @Override
    public void save(Group group) {
        if (!groupCreationValidator.test(group)) {
            throw new GroupCreationException("Group not valid!");
        }

        groupRepository.save(group);
        accountGroupInfoService.create(group.getCreatorId(), group.getId(), AccountRole.ADMIN);
        System.out.println("The group with name " + group.getName() + " saved!");
    }

    @Override
    public Group get(Integer id) {
        if (id == null) {
            throw new NullPointerException("The group id is null!");
        }

        return groupRepository.get(id);
    }

    @Override
    public void delete(Integer id) {
        Group group = get(id);
        if (group == null) {
            throw new GroupNotFoundException("The group with id = " + id + " does not exist!");
        }

        Set<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByGroupId(id);
        for (AccountGroupInfo info : accountGroupInfos) {
            accountGroupInfoService.deleteAccountGroupInfo(info);
        }

        groupRepository.delete(group.getId());
        System.out.println("The group with name " + group.getName() + " deleted!");
    }
}