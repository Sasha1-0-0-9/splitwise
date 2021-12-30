package com.example.repository;

import com.example.entity.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.example.common.IdGenerator.getExpenseCounter;

public class ExpenseRepository implements Repository<Expense> {

    private final static String FILE_NAME = "expenses.txt";

    @Override
    public void save(Expense expense) {
        RepositoryTemplate.save(getExpenseCounter(), expense, FILE_NAME);
    }

    @Override
    public Map<Integer, Expense> getAll() {
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

    public Set<Expense> get() {
        Map<Integer, Expense> expenses = getAll();

        return new HashSet<>(expenses.values());
    }
}