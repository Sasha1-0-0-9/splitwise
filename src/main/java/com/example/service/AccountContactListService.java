package com.example.service;

import com.example.entity.AccountContactList;
import com.example.repository.AccountContactListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountContactListService {

    private final AccountContactListRepository accountContactListRepository;

    @Autowired
    public AccountContactListService(AccountContactListRepository accountContactListRepository) {
        this.accountContactListRepository = accountContactListRepository;
    }

    /*public List<String> getAll() {
        List<AccountContactList> list = accountContactListRepository.getAll();
        return list.stream()
                .map(AccountContactList::getTelephoneNumber)
                .collect(Collectors.toList());
    }*/
}
