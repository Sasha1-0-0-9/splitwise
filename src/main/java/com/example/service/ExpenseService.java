package com.example.service;

import com.example.entity.*;
import com.example.repository.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class ExpenseService {

    private final ExpenseRepo expenseRepository;
    private final AccountGroupInfoService accountGroupInfoService;

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepository, AccountGroupInfoService accountGroupInfoService) {
        this.expenseRepository = expenseRepository;
        this.accountGroupInfoService = accountGroupInfoService;
    }

    public List<Expense> getAllAccountExpense(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException("Account id is null!");
        }

        List<Expense> accountExpenses = getExpensesByAccount(accountId);

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(accountId);
        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
            List<Expense> groupExpenses = getExpensesByGroup(accountGroupInfo.getGroupId());
            if (groupExpenses != null) {
                accountExpenses.addAll(groupExpenses);
            }
        }

        return accountExpenses;
    }

    public List<Expense> getExpensesByGroup(Integer groupId) {
        if (groupId == null) {
            throw new NullPointerException("Group id is null!");
        }

        return expenseRepository.findAll().stream()
                .filter(s -> s.getBorrowerId().equals(groupId) && s.getExpenseType() == ExpenseType.GROUP)
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByAccount(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException("Account id is null!");
        }

        return expenseRepository.findAll().stream()
                .filter(s -> (s.getBorrowerId().equals(accountId) || s.getLenderId().equals(accountId))
                        && s.getExpenseType() == ExpenseType.USER)
                .collect(Collectors.toList());
    }

    public Set<Expense> getExpensesForAccountByLender(Integer accountId, Integer lenderId) {
        if (accountId == null || lenderId == null) {
            throw new NullPointerException("Account id or lender id is null!");
        }

        return expenseRepository.findAll().stream()
                .filter(s -> s.getBorrowerId().equals(accountId) && s.getLenderId().equals(lenderId)
                        && s.getExpenseType() == ExpenseType.USER)
                .collect(Collectors.toSet());
    }

    public Set<Expense> getExpensesForGroupByLender(Integer groupId, Integer lenderId) {
        if (groupId == null || lenderId == null) {
            throw new NullPointerException("Group id or lender id is null!");
        }

        return expenseRepository.findAll().stream()
                .filter(s -> s.getBorrowerId().equals(groupId) && s.getLenderId().equals(lenderId)
                        && s.getExpenseType() == ExpenseType.GROUP)
                .collect(Collectors.toSet());
    }

    public Map<Integer, List<Expense>> getExpensesByGroups(Set<Integer> groupIds) {
        return groupIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(k -> k, this::getExpensesByGroup));
    }

    public Map<Integer, List<Expense>> getExpensesByAccounts(Set<Integer> accountIds) {
        return accountIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(k -> k, this::getExpensesByAccount));
    }

    public Expense get(Integer id) {
        return expenseRepository.getById(id);
    }

    public void save(@Valid Expense expense) {
        expenseRepository.save(expense);
    }
}