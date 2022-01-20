package com.example.repository.remove;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountContactRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Integer id, String phoneNumber) {
        return jdbcTemplate.update("INSERT INTO account_contacts (account_id, phone_number) VALUES(?, ?)", id,
                phoneNumber);
    }
}
