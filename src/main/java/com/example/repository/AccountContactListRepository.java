package com.example.repository;

import com.example.entity.AccountContactList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AccountContactListRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountContactListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getAll() {
        List<AccountContactList> list = jdbcTemplate.query("SELECT * FROM account_contact_list", new BeanPropertyRowMapper<>(AccountContactList.class));
        return list.stream()
                .map(AccountContactList::getTelephoneNumber)
                .collect(Collectors.toList());
    }
}
