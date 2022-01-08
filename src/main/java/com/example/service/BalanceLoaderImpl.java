package com.example.service;

import com.example.entity.*;
import com.example.entity.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.example.repository.ConverterRepository.converter;

@Service
public class BalanceLoaderImpl implements BalanceLoader {

    private final ExpenseService expenseService;
    private AccountGroupInfoService accountGroupInfoService;

    @Autowired
    public BalanceLoaderImpl(ExpenseService expenseService, AccountGroupInfoService accountGroupInfoService) {
        this.expenseService = expenseService;
        this.accountGroupInfoService = accountGroupInfoService;
    }

    BiPredicate<Integer, Integer> isNull = (x, y) -> (x == null || y == null
            || accountGroupInfoService.getAccountGroupInfo(x, y) == null);

    @Override
    public Balance getAccountBalance(Integer groupId, Integer accountId) {
        if (isNull.test(groupId, accountId)) {
            throw new NullPointerException("Group id or account id is null!");
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

    @Override
    public Balance getAccountBalance(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException("Account id is null!");
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

    @Override
    public Balance getAccountBalance(Integer groupId, Integer accountId, LocalDateTime afterDate) {
        if (isNull.test(groupId, accountId)) {
            throw new NullPointerException("Group id or account id is null!");
        }

        List<Expense> expensesByGroup = expenseService.getExpensesByGroup(groupId);

        int size = accountGroupInfoService.getAccountGroupInfosByGroupId(groupId).size();

        double amount = expensesByGroup.stream()
                .filter(s -> s.getLocalDateTime().isAfter(afterDate))
                .mapToDouble(s -> expenseByGroupToDouble(accountId, size, s))
                .sum();

        return new Balance(amount, Currency.USD);
    }

    private double expenseByGroupToDouble(Integer accountId, int size, Expense s) {
        double convert = converter(s.getCurrency(), Currency.USD);
        if (s.getLenderId().equals(accountId)) {
            return convert * s.getAmount() * (size - 1) / size;
        } else {
            return -convert * s.getAmount() / size;
        }
    }

    @Override
    public Balance getAccountBalance(Integer accountId, LocalDateTime afterDate) {
        if (accountId == null) {
            throw new NullPointerException("Account id is null!");
        }

        List<Expense> expensesByAccount = expenseService.getExpensesByAccount(accountId);
        double amount = expensesByAccount.stream()
                .filter(s -> s.getLocalDateTime().isAfter(afterDate))
                .mapToDouble(s -> expenseByAccountToDouble(accountId, s))
                .sum();

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(accountId);
        amount += accountGroupInfos.stream()
                .mapToDouble(s -> {
                    Balance balance = getAccountBalance(s.getGroupId(), s.getAccountId(), afterDate);
                    return (balance != null) ? balance.getAmount() : 0.0;
                })
                .sum();

        return new Balance(amount, Currency.USD);
    }

    private double expenseByAccountToDouble(Integer accountId, Expense s) {
        double convert = converter(s.getCurrency(), Currency.USD);
        if (s.getBorrowerId().equals(accountId)) {
            return -convert * s.getAmount();
        } else {
            return convert * s.getAmount();
        }
    }

    @Override
    public Map<Currency, Balance> getAccountBalanceByCurrencies(Integer groupId, Integer accountId) {
        if (isNull.test(groupId, accountId)) {
            throw new NullPointerException("Group id or account id is null!");
        }

        int size = accountGroupInfoService.getAccountGroupInfosByGroupId(groupId).size();

        List<Expense> expenses = expenseService.getExpensesByGroup(groupId);

        Map<Currency, List<Expense>> expensesByCurrencies = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getCurrency));

        return expensesByCurrencies.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, s -> new Balance(s.getValue().stream()
                        .mapToDouble(p -> expenseByGroupToDoubleByCurrencies(accountId, size, p))
                        .sum(), s.getKey())));
    }

    private double expenseByGroupToDoubleByCurrencies(Integer accountId, int size, Expense p) {
        if (p.getLenderId().equals(accountId)) {
            return p.getAmount() * (size - 1) / size;
        } else {
            return -p.getAmount() / size;
        }
    }

    @Override
    public Map<Currency, Balance> getAccountBalanceByCurrencies(Integer accountId) {
        if (accountId == null) {
            throw new NullPointerException("Account id is null!");
        }

        List<Expense> expensesByAccount = expenseService.getExpensesByAccount(accountId);

        Map<Currency, List<Expense>> expensesByAccountByCurrencies = expensesByAccount.stream()
                .collect(Collectors.groupingBy(Expense::getCurrency));

        Map<Currency, Balance> balancesByAccountByCurrencies = expensesByAccountByCurrencies.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, s -> new Balance(s.getValue().stream()
                        .mapToDouble(p -> expenseByAccountToDoubleByCurrencies(accountId, p))
                        .sum(), s.getKey())));

        List<AccountGroupInfo> accountGroupInfos = accountGroupInfoService.getAccountGroupInfosByAccountId(accountId);

        Map<Currency, Balance> balancesByGroupsByCurrencies = new HashMap<>();

        for (AccountGroupInfo accountGroupInfo : accountGroupInfos) {
            Map<Currency, Balance> balancesByGroupByCurrencies
                    = getAccountBalanceByCurrencies(accountGroupInfo.getGroupId(), accountId);
            balancesByGroupByCurrencies.forEach((key, value) -> balancesByGroupsByCurrencies
                    .merge(key, value, (a, b) -> new Balance(a.getAmount() + b.getAmount(), key)));
        }

        balancesByGroupsByCurrencies.forEach((key, value) -> balancesByAccountByCurrencies
                .merge(key, value, (a, b) -> new Balance(a.getAmount() + b.getAmount(), key)));

        return balancesByAccountByCurrencies;
    }

    private double expenseByAccountToDoubleByCurrencies(Integer accountId, Expense p) {
        if (p.getLenderId().equals(accountId)) {
            return p.getAmount();
        } else {
            return -p.getAmount();
        }
    }
}