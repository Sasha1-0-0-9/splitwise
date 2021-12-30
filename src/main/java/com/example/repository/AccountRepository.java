package com.example.repository;

import com.example.entity.Account;

import java.util.Map;
import java.util.Set;

import static com.example.common.IdGenerator.getAccountCounter;

@org.springframework.stereotype.Repository
public class AccountRepository implements Repository<Account> {

    private final static String FILE_NAME = "accounts.txt";

    @Override
    public void save(Account account) {
        int newAccountId = getAccountCounter();
        account.setAccountId(newAccountId);
        RepositoryTemplate.save(newAccountId, account, FILE_NAME);
    }

    @Override
    public Map<Integer, Account> getAll() {
        return RepositoryTemplate.getItems(FILE_NAME);
    }

    @Override
    public void deleteAll(Set<Integer> ids) {
        RepositoryTemplate.deleteAll(ids, FILE_NAME);
    }

    @Override
    public int getSize() {
        return RepositoryTemplate.getSize(FILE_NAME);
    }
}