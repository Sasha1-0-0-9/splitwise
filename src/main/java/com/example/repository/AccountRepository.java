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

    public int save(Account account) {
        jdbcTemplate.update("INSERT INTO accounts (telephoneNumber, encodedPassword) VALUES(?, ?)", account.getTelephoneNumber(),
                account.getEncodedPassword());
        return getByNameAndPassword(account.getTelephoneNumber(), account.getEncodedPassword()).getId();
    }

    /*public List<Account> getAll() {
        return jdbcTemplate.query("SELECT * FROM accounts", new BeanPropertyRowMapper<>(Account.class));
    }*/

    public Account get(int id) {
        return jdbcTemplate.query("SELECT * FROM accounts WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Account.class))
                .stream().findAny().orElse(null);
    }

    public Account getByNameAndPassword(String telephoneNumber, String encodedPassword) {
        List<Account> accounts = jdbcTemplate.query("SELECT * FROM accounts WHERE telephoneNumber=? AND encodedPassword=?", new BeanPropertyRowMapper<>(Account.class), telephoneNumber, encodedPassword);
        return accounts.stream().findAny().orElse(null);
    }

    public Account getByTelephoneNumber(String telephoneNumber) {
        List<Account> accounts = jdbcTemplate.query("SELECT * FROM accounts WHERE telephoneNumber=?",
                new BeanPropertyRowMapper<>(Account.class), telephoneNumber);
        return accounts.stream().findAny().orElse(null);
    }

    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM accounts WHERE id=?", id);

    }
}