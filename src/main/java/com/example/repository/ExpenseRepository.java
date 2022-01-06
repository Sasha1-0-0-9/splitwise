package com.example.repository;

import com.example.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExpenseRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExpenseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Expense expense) {
        jdbcTemplate.update("INSERT INTO expenses VALUES(?, ?, ?)", expense.getLenderId(), expense.getBorrowerId(),
                expense.getAmount(), expense.getExpenseType().name(), expense.getLocalDateTime(),
                expense.getCurrency().name());
    }

    public List<Expense> getAll() {
        List<Expense> list = jdbcTemplate.query("SELECT * FROM expenses", new BeanPropertyRowMapper<>(Expense.class));
        return list;
    }

    public Expense get(int id) {
        return jdbcTemplate.query("SELECT * FROM expenses WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Expense.class))
                .stream().findAny().orElse(null);
    }
}