package com.example.service;

import com.example.entity.*;
import com.example.entity.Currency;
import com.example.repository.ExpenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.entity.ExpenseType.USER;

@Service
@Validated
public class ExpenseService {

    private final ExpenseRepo expenseRepository;
    private final AccountGroupInfoService accountGroupInfoService;
    private final AccountService accountService;
    private final GroupService groupService;

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepository, AccountGroupInfoService accountGroupInfoService,
                          AccountService accountService, GroupService groupService) {
        this.expenseRepository = expenseRepository;
        this.accountGroupInfoService = accountGroupInfoService;
        this.accountService = accountService;
        this.groupService = groupService;
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
        return expenseRepository.findAll().stream()
                .filter(s -> s.getBorrowerId().equals(groupId) && s.getExpenseType() == ExpenseType.GROUP)
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByAccount(Integer accountId) {
        return expenseRepository.findAll().stream()
                .filter(s -> (s.getBorrowerId().equals(accountId) || s.getLenderId().equals(accountId))
                        && s.getExpenseType() == ExpenseType.USER)
                .collect(Collectors.toList());
    }

    public Expense get(Integer id) {
        return expenseRepository.getById(id);
    }

    public void save(@Valid Expense expense) {
        expenseRepository.save(expense);
    }

    public String getExpenseName(Expense expense) {
        String name;
        if (expense.getExpenseType() == USER) {
            Account account = accountService.get(expense.getBorrowerId());
            name = account.getTelephoneNumber();
        } else {
            Group group = groupService.get(expense.getBorrowerId());
            name = group.getName();
        }

        return name;
    }

    public void create(String amount, String currency, String expenseType, String lenderName,
                       String borrowerName, Integer accountId) {
        Account lender = accountService.getByTelephoneNumber(lenderName);
        ExpenseType expenseType1 = ExpenseType.valueOf(expenseType);
        Integer borrowerId;

        if (expenseType1 == USER) {
            Account borrower = accountService.getByTelephoneNumber(borrowerName);
            borrowerId = borrower.getId();
        } else {
            Group borrower = groupService.getAllByAccountIdAndName(accountId, borrowerName);
            borrowerId = borrower.getId();
        }

        Expense expense = new Expense.ExpenseBuilder()
                .setLenderId(lender.getId())
                .setAmount(Double.parseDouble(amount))
                .setExpenseType(expenseType1)
                .setLocalDateTime(Timestamp.valueOf(LocalDateTime.now()))
                .setCurrency(Currency.valueOf(currency))
                .setBorrowerId(borrowerId)
                .build();

        save(expense);
    }
}