package com.example.repository;

import com.example.entity.AccountGroupInfo;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.common.IdGenerator.getAccountGroupInfoCounter;

@org.springframework.stereotype.Repository
public class AccountGroupInfoRepository implements Repository<AccountGroupInfo> {

    private final static String FILE_NAME = "account_group_infos.txt";

    @Override
    public void save(AccountGroupInfo accountGroupInfo) {
        RepositoryTemplate.save(getAccountGroupInfoCounter(), accountGroupInfo, FILE_NAME);
    }

    @Override
    public Map<Integer, AccountGroupInfo> getAll() {
        return RepositoryTemplate.getItems(FILE_NAME);
    }

    @Override
    public void deleteAll(Set<Integer> ids) {
        RepositoryTemplate.deleteAll(ids, FILE_NAME);
    }

    public AccountGroupInfo get(Integer accountId, Integer groupId) {
        Map<Integer, AccountGroupInfo> accountGroupInfos = getAll();

        Map.Entry<Integer, AccountGroupInfo> accountGroupInfoById = accountGroupInfos.entrySet().stream()
                .filter(s -> (s.getValue().getAccountId().equals(accountId))
                        && (s.getValue().getGroupId().equals(groupId)))
                .findFirst().orElse(null);

        return (accountGroupInfoById == null) ? null : accountGroupInfoById.getValue();
    }

    public Set<AccountGroupInfo> get(Integer id, boolean byAccountId) {
        Map<Integer, AccountGroupInfo> accountGroupInfos = getAll();

        return accountGroupInfos.values().stream()
                .filter(accountGroupInfo -> (accountGroupInfo.getAccountId().equals(id) && byAccountId)
                        || (accountGroupInfo.getGroupId().equals(id) && !byAccountId))
                .collect(Collectors.toSet());
    }

    public void delete(AccountGroupInfo accountGroupInfo) {
        Map<Integer, AccountGroupInfo> accountGroupInfos = getAll();

        int accountGroupInfoId = accountGroupInfos.entrySet().stream()
                .filter(s -> (s.getValue().equals(accountGroupInfo)))
                .findFirst().get().getKey();

        deleteAll(Set.of(accountGroupInfoId));
    }

    @Override
    public int getSize() {
        return RepositoryTemplate.getSize(FILE_NAME);
    }
}