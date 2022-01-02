package com.example.service;

import com.example.common.SortType;
import com.example.entity.*;
import com.example.entity.Currency;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseHistoryPrinterImpl implements ExpenseHistoryPrinter {

    private final BalanceLoader balanceLoader;
    private final ExpenseTracker expenseTracker;
    private final AccountService accountService;
    private final GroupService groupService;

    public ExpenseHistoryPrinterImpl(BalanceLoader balanceLoader, ExpenseTracker expenseTracker,
                                     AccountService accountService, GroupService groupService) {
        this.balanceLoader = balanceLoader;
        this.expenseTracker = expenseTracker;
        this.accountService = accountService;
        this.groupService = groupService;
    }

    static class ExpenseComparator implements Comparator<Expense> {

        SortType sortType;

        public ExpenseComparator(SortType sortType) {
            this.sortType = sortType;
        }

        public int compare(Expense a, Expense b) {
            int val = (sortType == SortType.ASC) ? 1 : -1;
            return val * (a.getLocalDateTime().compareTo(b.getLocalDateTime()));
        }
    }

    @Override
    public void printAccountExpenseHistory(Integer accountId, SortType sortType) {
        Set<Expense> expenses = expenseTracker.getExpensesByAccount(accountId);

        ExpenseComparator comparator = new ExpenseComparator(sortType);

        Set<Expense> sortedExpenses = new TreeSet<>(comparator);
        sortedExpenses.addAll(expenses);

        Account account = accountService.get(accountId);

        StringBuilder str = new StringBuilder(/*account.ge.getName() +*/ "'s Expense History\n");

        for (Expense expense : sortedExpenses) {
            if (expense.getLenderId().equals(accountId)) {
                str.append(expense.getLocalDateTime().toString())
                        .append(" Lent ")
                        .append(expense.getAmount())
                        .append(" ")
                        .append(expense.getCurrency())
                        .append("\n");
            } else {
                str.append(expense.getLocalDateTime().toString())
                        .append(" Borrowed ")
                        .append(expense.getAmount())
                        .append(" ")
                        .append(expense.getCurrency())
                        .append("\n");
            }
        }

        Balance balance = balanceLoader.getAccountBalance(accountId);
        str.append("Total: ")
                .append(balance.getAmount())
                .append(" ")
                .append(balance.getCurrency());

        System.out.println(str);
    }

    @Override
    public void printGroupExpenseHistory(Integer groupId, SortType sortType) {
        Set<Expense> expenses = expenseTracker.getExpensesByGroup(groupId);

        ExpenseComparator comparator = new ExpenseComparator(sortType);

        Set<Expense> sortedExpenses = new TreeSet<>(comparator);
        sortedExpenses.addAll(expenses);

        Group group = groupService.get(groupId);

        StringBuilder str = new StringBuilder(group.getName() + "'s Expense History\n");

        for (Expense expense : sortedExpenses) {
            str.append(expense.getLocalDateTime().toString())
                    .append(" Lent ")
                    .append(expense.getAmount())
                    .append(" ")
                    .append(expense.getCurrency())
                    .append("\n");
        }

        System.out.println(str);
    }

    @Override
    public void printAccountExpenseHistoryGroupByCurrencies(Integer accountId, SortType sortType) {
        Set<Expense> expenses = expenseTracker.getExpensesByAccount(accountId);

        ExpenseComparator comparator = new ExpenseComparator(sortType);

        Set<Expense> sortedExpenses = expenses.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));

        Map<Currency, List<Expense>> sortedExpensesByCurrencies = sortedExpenses.stream()
                .collect(Collectors.groupingBy(Expense::getCurrency));

        Balance balance = balanceLoader.getAccountBalance(accountId);

        printAccountExpenseHistoryGroup(accountId, sortedExpensesByCurrencies, balance);
    }

    @Override
    public void printGroupExpenseHistoryGroupByCurrencies(Integer groupId, SortType sortType) {
        Set<Expense> expenses = expenseTracker.getExpensesByGroup(groupId);

        ExpenseComparator comparator = new ExpenseComparator(sortType);

        Set<Expense> sortedExpenses = expenses.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));

        Map<Currency, List<Expense>> sortedExpensesByCurrencies = sortedExpenses.stream()
                .collect(Collectors.groupingBy(Expense::getCurrency));

        printGroupExpenseHistoryGroup(groupId, sortedExpensesByCurrencies);
    }

    @Override
    public void printAccountExpenseHistoryGroupByCurrencies(Integer accountId, SortType sortType, LocalDateTime afterDate) {
        Set<Expense> expenses = expenseTracker.getExpensesByAccount(accountId);

        ExpenseComparator comparator = new ExpenseComparator(sortType);

        Set<Expense> sortedExpenses = expenses.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));

        Map<Currency, List<Expense>> sortedExpensesByCurrencies = sortedExpenses.stream()
                .filter(s -> s.getLocalDateTime().isAfter(afterDate))
                .collect(Collectors.groupingBy(Expense::getCurrency));

        Balance balance = balanceLoader.getAccountBalance(accountId, afterDate);

        printAccountExpenseHistoryGroup(accountId, sortedExpensesByCurrencies, balance);
    }

    @Override
    public void printGroupExpenseHistoryGroupByCurrencies(Integer groupId, SortType sortType, LocalDateTime afterDate) {
        Set<Expense> expenses = expenseTracker.getExpensesByGroup(groupId);

        ExpenseComparator comparator = new ExpenseComparator(sortType);

        Set<Expense> sortedExpenses = expenses.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(comparator)));

        Map<Currency, List<Expense>> sortedExpensesByCurrencies = sortedExpenses.stream()
                .filter(s -> s.getLocalDateTime().isAfter(afterDate))
                .collect(Collectors.groupingBy(Expense::getCurrency));

        printGroupExpenseHistoryGroup(groupId, sortedExpensesByCurrencies);
    }

    private void printGroupExpenseHistoryGroup(Integer groupId, Map<Currency, List<Expense>> sortedExpensesByCurrencies) {
        Group group = groupService.get(groupId);

        StringBuilder str = new StringBuilder(group.getName() + "'s Expense History\n");

        for (Map.Entry<Currency, List<Expense>> entry : sortedExpensesByCurrencies.entrySet()) {
            str.append(entry.getKey().toString())
                    .append("\n");
            entry.getValue().forEach(s -> str.append(s.getLocalDateTime().toString())
                    .append(" Lent ")
                    .append(s.getAmount())
                    .append("\n"));
        }

        System.out.println(str);
    }

    private void printAccountExpenseHistoryGroup(Integer accountId,
                                                 Map<Currency, List<Expense>> sortedExpensesByCurrencies,
                                                 Balance balance) {
        Account account = accountService.get(accountId);

        StringBuilder str = new StringBuilder(/*account.getName() +*/ "'s Expense History\n");

        for (Map.Entry<Currency, List<Expense>> entry : sortedExpensesByCurrencies.entrySet()) {
            str.append(entry.getKey().toString())
                    .append("\n");
            entry.getValue().forEach(s -> str.append(s.getLocalDateTime().toString())
                    .append((s.getLenderId().equals(accountId)) ? " Lent " : " Borrowed ")
                    .append(s.getAmount())
                    .append("\n"));
        }

        str.append("Total: ")
                .append(balance.getAmount())
                .append(" ")
                .append(balance.getCurrency());

        System.out.println(str);
    }
}
