package com.example.service;

import com.example.common.validator.GroupCreationValidator;
import com.example.entity.*;
import com.example.exception.GroupCreationException;
import com.example.exception.GroupNotFoundException;
import com.example.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupRepository groupRepository;
    private final GroupCreationValidator groupCreationValidator;

    @Autowired
    public GroupService(AccountGroupInfoService accountGroupInfoService, GroupRepository groupRepository,
                        GroupCreationValidator groupCreationValidator) {
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

    public void save(Group group) {
        if (!groupCreationValidator.test(group)) {
            throw new GroupCreationException("Group not valid!");
        }

        groupRepository.save(group);
        accountGroupInfoService.create(group.getCreatorId(), group.getId(), AccountRole.ADMIN);
        System.out.println("The group with name " + group.getName() + " saved!");
    }

    public Group get(Integer id) {
        if (id == null) {
            throw new NullPointerException("The group id is null!");
        }

        return groupRepository.get(id);
    }

    public List<Group> getAll() {
        return groupRepository.getAll();
    }

    public List<Group> getAllByAccountId(Integer id) {
        return groupRepository.getAllByAccountId(id);
    }

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

    public void update(int id, Group group) {
        groupRepository.update(id, group);
    }

    public List<Contact> getGroupContacts(Integer id) {
        return groupRepository.getContactsByGroupId(id);
    }
}