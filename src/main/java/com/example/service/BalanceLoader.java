package com.example.service;

import com.example.entity.*;
import com.example.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiPredicate;

import static com.example.repository.ConverterRepository.converter;

@Service
public class BalanceLoader {

    private final ExpenseService expenseService;
    private AccountGroupInfoService accountGroupInfoService;

    @Autowired
    public BalanceLoader(ExpenseService expenseService, AccountGroupInfoService accountGroupInfoService) {
        this.expenseService = expenseService;
        this.accountGroupInfoService = accountGroupInfoService;
    }

    BiPredicate<Integer, Integer> isNull = (x, y) -> (x == null || y == null
            || accountGroupInfoService.getAccountGroupInfo(x, y) == null);

    public Balance getAccountBalance(Integer groupId, Integer accountId) {
        if (isNull.test(groupId, accountId)) {
            throw new RuntimeException("Group id or account id is null!");
        }

        double amount = 0;

        List<Expense> groupExpenses = expenseService.getExpensesByGroup(groupId);
        for (Expense expense : groupExpenses) {
            int size = accountGroupInfoService.getAccountGroupInfosByGroupId(groupId).size();
            double convert = converter(expense.getCurrency(), Currency.USD);
            if (expense.getLenderId().equals(accountId)) {
                amount += convert * expense.getAmount() * (size - 1) / size;
            } else {
                amount -= convert * expense.getAmount() / size;
            }
        }

        return new Balance(amount, Currency.USD);
    }

    public Balance getAccountBalance(Integer accountId) {
        if (accountId == null) {
            throw new RuntimeException("Account id is null!");
        }

        double amount = 0;

        List<Expense> accountExpenses = expenseService.getExpensesByAccount(accountId);
        for (Expense expense : accountExpenses) {
            double convert = converter(expense.getCurrency(), Currency.USD);
            if (expense.getBorrowerId().equals(accountId)) {
                amount -= convert * expense.getAmount();
            } else {
                amount += convert * expense.getAmount();
            }
        }

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(accountId);
        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
            Balance balance = getAccountBalance(accountGroupInfo.getGroupId(), accountGroupInfo.getAccountId());
            if (balance != null) {
                amount += balance.getAmount();
            }
        }

        return new Balance(amount, Currency.USD);
    }
}