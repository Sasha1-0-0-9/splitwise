package com.example;

import com.example.common.SortType;
import com.example.common.validator.AccountCreationValidator;
import com.example.common.validator.GroupCreationValidator;
import com.example.entity.*;
import com.example.repository.*;
import com.example.service.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

public class App {

    public static void main(String[] args) {

        cleaner();

        User user1 = new User("user1", "1111", "user1@gmail.com");
        User user2 = new User("user2", "2222", "user2@gmail.com");
        User user3 = new User("user3", "3333", "user3@gmail.com");

        AccountGroupInfoRepository accountGroupInfoRepository = new AccountGroupInfoRepository();

        AccountGroupInfoService accountGroupInfoService = new AccountGroupInfoServiceImpl(accountGroupInfoRepository);

        Repository<Group> groupRepository = new GroupRepository();

        GroupCreationValidator groupCreationValidator = new GroupCreationValidator();

        GroupService groupService = new GroupService(accountGroupInfoService, groupRepository, groupCreationValidator);

        Repository<Account> accountRepository = new AccountRepository();

        AccountCreationValidator accountCreationValidator = new AccountCreationValidator();

        AccountService accountService = new AccountService(accountRepository, groupService, accountGroupInfoService, accountCreationValidator);

        Account account1 = new Account(user1);
        accountService.save(account1);
        Integer account1Id = account1.getId();

        Account account2 = new Account(user2);
        accountService.save(account2);
        Integer account2Id = account2.getId();

        Account account3 = new Account(user3);
        accountService.save(account3);
        Integer account3Id = account3.getId();

        Group group1 = new Group("group1", account1Id);
        groupService.save(group1);
        Integer group1Id = group1.getId();

        Group group2 = new Group("group2", account2Id);
        groupService.save(group2);
        Integer group2Id = group2.getId();

        groupService.addToGroup(group1Id, account1Id, account2Id);
        groupService.addToGroup(group1Id, account1Id, account3Id);
        groupService.addToGroup(group2Id, account2Id, account1Id);
        groupService.addToGroup(group2Id, account2Id, account3Id);
        printInfo(accountService);
        printInfo(groupService);
        printInfo(accountGroupInfoService);

        ExpenseRepository expenseRepository = new ExpenseRepository();

        ExpenseTracker expenseTracker = new ExpenseTrackerImpl(expenseRepository);

        Expense expense1 = new Expense.ExpenseBuilder()
                .setLenderId(account1Id)
                .setAmount(100.0)
                .setExpenseType(ExpenseType.USER)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 1, 0, 0, 0))
                .setCurrency(Currency.USD)
                .setBorrowerId(account2Id)
                .build();

        Expense expense2 = new Expense.ExpenseBuilder()
                .setLenderId(account1Id)
                .setAmount(200.0)
                .setExpenseType(ExpenseType.USER)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 2, 0, 0, 0))
                .setCurrency(Currency.USD)
                .setBorrowerId(account3Id)
                .build();

        Expense expense3 = new Expense.ExpenseBuilder()
                .setLenderId(account2Id)
                .setAmount(50.0)
                .setExpenseType(ExpenseType.USER)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 3, 0, 0, 0))
                .setCurrency(Currency.USD)
                .setBorrowerId(account3Id)
                .build();

        Expense expense6 = new Expense.ExpenseBuilder()
                .setLenderId(account1Id)
                .setAmount(100.0)
                .setExpenseType(ExpenseType.USER)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 6, 0, 0, 0))
                .setCurrency(Currency.EUR)
                .setBorrowerId(account2Id)
                .build();

        expenseTracker.trackExpense(expense1);
        expenseTracker.trackExpense(expense2);
        expenseTracker.trackExpense(expense3);
        expenseTracker.trackExpense(expense6);

        BalanceLoader balanceLoader = new BalanceLoaderImpl(expenseTracker, accountGroupInfoService);
        System.out.println(account1.getUser().getName() + " " + balanceLoader.getAccountBalance(account1Id).toString());
        ExpenseHistoryPrinter historyPrinter = new ExpenseHistoryPrinterImpl(balanceLoader, expenseTracker,
                accountService, groupService);
        historyPrinter.printAccountExpenseHistory(account1Id, SortType.ASC);

        historyPrinter.printAccountExpenseHistoryGroupByCurrencies(account1Id, SortType.DESC);

        historyPrinter.printAccountExpenseHistoryGroupByCurrencies(account1Id, SortType.DESC,
                LocalDateTime.of(2021, 10, 1, 0, 0, 0));

        Expense expense4 = new Expense.ExpenseBuilder()
                .setLenderId(account1Id)
                .setAmount(300.0)
                .setExpenseType(ExpenseType.GROUP)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 4, 0, 0, 0))
                .setCurrency(Currency.USD)
                .setBorrowerId(group1Id)
                .build();

        expenseTracker.trackExpense(expense4);

        Expense expense5 = new Expense.ExpenseBuilder()
                .setLenderId(account1Id)
                .setAmount(600.0)
                .setExpenseType(ExpenseType.GROUP)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 5, 0, 0, 0))
                .setCurrency(Currency.USD)
                .setBorrowerId(group1Id)
                .build();

        expenseTracker.trackExpense(expense5);

        Expense expense7 = new Expense.ExpenseBuilder()
                .setLenderId(account1Id)
                .setAmount(600.0)
                .setExpenseType(ExpenseType.GROUP)
                .setLocalDateTime(LocalDateTime.of(2021, 10, 7, 0, 0, 0))
                .setCurrency(Currency.EUR)
                .setBorrowerId(group1Id)
                .build();

        expenseTracker.trackExpense(expense7);

        System.out.println(account2.getUser().getName() + " in " + group1.getName() + " "
                + balanceLoader.getAccountBalance(group1Id, account2Id).toString());

        System.out.println(account2.getUser().getName() + " in " + group1.getName() + " "
                + balanceLoader.getAccountBalance(group1Id, account2Id,
                LocalDateTime.of(2021, 10, 4, 0, 0, 0)).toString());

        System.out.println(account1.getUser().getName() + " " + balanceLoader.getAccountBalance(account1Id).toString());

        System.out.println(account1.getUser().getName() + " "
                + balanceLoader.getAccountBalance(account1Id,
                LocalDateTime.of(2021, 10, 4, 0, 0, 0)).toString());

        System.out.println(account1.getUser().getName() + " in " + group1.getName() + " "
                + balanceLoader.getAccountBalanceByCurrencies(group1Id, account1Id).toString());

        System.out.println(account1.getUser().getName() + " " + balanceLoader.getAccountBalanceByCurrencies(account1Id).toString());

        historyPrinter.printGroupExpenseHistory(group1Id, SortType.DESC);

        historyPrinter.printGroupExpenseHistoryGroupByCurrencies(group1Id, SortType.DESC);

        historyPrinter.printGroupExpenseHistoryGroupByCurrencies(group1Id, SortType.DESC,
                LocalDateTime.of(2021, 10, 4, 0, 0, 0));

        groupService.leaveGroup(group2Id, account3Id);
        new DeleteGroupService(accountGroupInfoService, groupService).delete(group2Id, account2Id);
        printInfo(accountService);
        printInfo(groupService);
        printInfo(accountGroupInfoService);

        accountService.delete(account1Id);
        printInfo(accountService);
        printInfo(groupService);
        printInfo(accountGroupInfoService);

        System.out.println(expenseTracker.getExpensesByAccounts(Set.of(account1Id, account2Id)).size());
        System.out.println(expenseTracker.getExpensesByGroups(Set.of(group1Id, group2Id)).size());
        System.out.println(expenseTracker.getExpensesForAccountByLender(account2Id, account1Id).size());
        System.out.println(expenseTracker.getExpensesForGroupByLender(group1Id, account1Id).size());
    }

    private static <T> void printInfo(T t) {
        System.out.println(t.toString());
    }

    private static void cleaner() {
        String pathFile = "src/main/resources/";
        try (FileWriter writer1 = new FileWriter(pathFile + "groups.txt");
             FileWriter writer2 = new FileWriter(pathFile + "accounts.txt");
             FileWriter writer4 = new FileWriter(pathFile + "account_group_infos.txt");
             FileWriter writer5 = new FileWriter(pathFile + "expenses.txt")) {
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}