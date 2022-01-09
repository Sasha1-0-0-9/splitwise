package com.example.service;

import com.example.common.validator.GroupCreationValidator;
import com.example.entity.*;
import com.example.exception.GroupCreationException;
import com.example.exception.GroupNotFoundException;
import com.example.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class GroupService {

    private final AccountGroupInfoService accountGroupInfoService;
    private final GroupRepository groupRepository;
    private final GroupCreationValidator groupCreationValidator;
    private final ContactService contactService;

    @Autowired
    public GroupService(AccountGroupInfoService accountGroupInfoService, GroupRepository groupRepository,
                        GroupCreationValidator groupCreationValidator, ContactService contactService) {
        this.accountGroupInfoService = accountGroupInfoService;
        this.groupRepository = groupRepository;
        this.groupCreationValidator = groupCreationValidator;
        this.contactService = contactService;
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
        if (accountGroupInfo.getAccountRole() == AccountRole.USER) {
            accountGroupInfoService.deleteAccountGroupInfo(accountGroupInfo);
        } else {
            delete(groupId);
        }
    }

    public void save(@Valid Group group) {
        /*if (!groupCreationValidator.test(group)) {
            throw new GroupCreationException("Group not valid!");
        }*/

        Integer id = groupRepository.save(group);
        accountGroupInfoService.create(group.getCreatorId(), id, AccountRole.ADMIN);
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
        List<AccountGroupInfo> list = accountGroupInfoService.getAccountGroupInfosByAccountId(id);
        return list.stream()
                .map(s -> groupRepository.get(s.getGroupId()))
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        /*Group group = get(id);
        if (group == null) {
            throw new GroupNotFoundException("The group with id = " + id + " does not exist!");
        }*/

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByGroupId(id);
        for (AccountGroupInfo info : accountGroupInfos) {
            accountGroupInfoService.deleteAccountGroupInfo(info);
        }

        groupRepository.delete(id);
        //System.out.println("The group with name " + group.getName() + " deleted!");
    }

    public void update(int id, Group group) {
        groupRepository.update(id, group);
    }

    public List<Integer> getIdAccounts(Integer id) {
        List<AccountGroupInfo> list = accountGroupInfoService.getAccountGroupInfosByGroupId(id);
        return list.stream()
                .map(s -> s.getAccountId())
                .collect(Collectors.toList());
    }

    public Group getAllByAccountIdAndName(Integer accountId, String lenderName) {
        List<Group> groups = groupRepository.getAllByAccountId(accountId);
        return groups.stream()
                .filter(s -> s.getName().equals(lenderName))
                .findAny().orElse(null);
    }
}