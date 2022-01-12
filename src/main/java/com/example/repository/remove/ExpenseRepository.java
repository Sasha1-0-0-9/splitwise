package com.example.repository.remove;

import com.example.entity.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExpenseRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExpenseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Expense expense) {
        jdbcTemplate.update("INSERT INTO expenses (lenderid, borrowerid, amount, time, currency, expensetype) VALUES(?, ?, ?, ?, ?, ?)",
                expense.getLenderId(), expense.getBorrowerId(), expense.getAmount(),
                expense.getLocalDateTime(), expense.getCurrency().toString(), expense.getExpenseType().toString());
    }

    public List<Expense> getAll() {
        List<Expense> list = jdbcTemplate.query("SELECT * FROM expenses", new BeanPropertyRowMapper<>(Expense.class));
        return list;
    }

    public List<Expense> getByAccountId(Integer id) {
        List<Expense> list = jdbcTemplate.query("SELECT * FROM expenses AS e"
                + " INNER JOIN account_group_info AS a_g_i ON a_g_i.groupId = e.borrowerid"
                + " WHERE a_g_i.accountid = ?", new BeanPropertyRowMapper<>(Expense.class), id);
        return list.stream()
                .filter(s -> s.getExpenseType() == ExpenseType.GROUP)
                .collect(Collectors.toList());
    }

    public Expense get(int id) {
        return jdbcTemplate.query("SELECT * FROM expenses WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Expense.class))
                .stream().findAny().orElse(null);
    }
}