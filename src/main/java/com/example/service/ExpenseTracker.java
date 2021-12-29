package com.example.service;

import com.example.entity.*;

import java.util.Map;
import java.util.Set;

public interface ExpenseTracker {

    void trackExpense(Expense expense);

    Set<Expense> getExpensesByGroup(Integer groupId);

    Set<Expense> getExpensesByAccount(Integer accountId);

    Set<Expense> getExpensesForAccountByLender(Integer accountId, Integer lenderId);

    Set<Expense> getExpensesForGroupByLender(Integer groupId, Integer lenderId);

    Map<Integer, Set<Expense>> getExpensesByGroups(Set<Integer> groupIds);

    Map<Integer, Set<Expense>> getExpensesByAccounts(Set<Integer> accountIds);
}