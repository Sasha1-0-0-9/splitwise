package com.example.service;

import com.example.entity.*;
import com.example.exception.GroupNotFoundException;
import com.example.repository.GroupRepo;
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
    private final GroupRepo groupRepository;

    @Autowired
    public GroupService(AccountGroupInfoService accountGroupInfoService, GroupRepo groupRepository) {
        this.accountGroupInfoService = accountGroupInfoService;
        this.groupRepository = groupRepository;
    }

    public void addToGroup(Integer groupId, Integer accountId, Integer addedBy) {
        Group group = get(groupId);
        if (group == null) {
            throw new GroupNotFoundException("The group with id = " + groupId + " does not exist!");
        }

        AccountGroupInfo accountGroupInfo = accountGroupInfoService.getAccountGroupInfo(groupId, accountId);
        if ((accountGroupInfo != null) && (groupRepository.getById(groupId).getCreatorId().equals(accountId))) {
            accountGroupInfoService.create(addedBy, groupId);
        }
    }

    public void leaveGroup(Integer groupId, Integer accountId) {
        if (accountId == null || groupId == null) {
            throw new NullPointerException("The group id or account id is null!");
        }

        AccountGroupInfo accountGroupInfo = accountGroupInfoService.getAccountGroupInfo(groupId, accountId);
        if (!groupRepository.getById(groupId).getCreatorId().equals(accountId)) {
            accountGroupInfoService.deleteAccountGroupInfo(accountGroupInfo);
        } else {
            delete(groupId);
        }
    }

    public void save(@Valid Group group) {
        //Integer id = groupRepository.save(group);
        groupRepository.save(group);
        accountGroupInfoService.create(group.getCreatorId(), group.getId());
    }

    public Group get(Integer id) {
        if (id == null) {
            throw new NullPointerException("The group id is null!");
        }

        return groupRepository.getById(id);
    }

    public List<Group> getAllByAccountId(Integer id) {
        List<AccountGroupInfo> list = accountGroupInfoService.getAccountGroupInfosByAccountId(id);
        return list.stream()
                .map(s -> groupRepository.getById(s.getGroupId()))
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByGroupId(id);
        for (AccountGroupInfo info : accountGroupInfos) {
            accountGroupInfoService.deleteAccountGroupInfo(info);
        }

        groupRepository.deleteById(id);
    }

    public List<Integer> getIdAccounts(Integer id) {
        List<AccountGroupInfo> list = accountGroupInfoService.getAccountGroupInfosByGroupId(id);
        return list.stream()
                .map(AccountGroupInfo::getAccountId)
                .collect(Collectors.toList());
    }

    public Group getAllByAccountIdAndName(Integer accountId, String lenderName) {
        List<Group> groups = groupRepository.getAllByAccountId(accountId);
        return groups.stream()
                .filter(s -> s.getName().equals(lenderName))
                .findAny().orElse(null);
    }
}