package com.example.repository;

import com.example.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Account account) {
        jdbcTemplate.update("INSERT INTO Account VALUES(?, ?, ?)", account.getName(), account.getTelephoneNumber(),
                account.getEmail());
    }

    public List<Account> getAll() {
        return jdbcTemplate.query("SELECT * FROM Account", new BeanPropertyRowMapper<>(Account.class));
    }

    public Account get(int id) {
        return jdbcTemplate.query("SELECT * FROM Account WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Account.class))
                .stream().findAny().orElse(null);
    }

    public void update(int id, Account updatedAccount) {
        jdbcTemplate.update("UPDATE Account SET name=?, telephoneNumber=?, email=? WHERE id=?", updatedAccount.getName(),
                updatedAccount.getTelephoneNumber(), updatedAccount.getEmail(), id);
    }

    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM Account WHERE id=?", id);

    }
}