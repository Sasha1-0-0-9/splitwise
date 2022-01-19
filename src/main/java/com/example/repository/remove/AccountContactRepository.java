package com.example.repository.remove;

import com.example.entity.Contact;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountContactRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contact> getByAccountId(Integer accountId) {
        return jdbcTemplate.query("SELECT * FROM account_contacts WHERE account_id = ?", new BeanPropertyRowMapper<>(Contact.class), accountId);
    }
}
