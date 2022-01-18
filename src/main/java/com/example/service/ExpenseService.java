package com.example.service;

import com.example.entity.*;
import com.example.entity.Currency;
import com.example.repository.ExpenseRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
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
    private final ContactService contactService;
    private final GroupService groupService;

    @Autowired
    public ExpenseService(ExpenseRepo expenseRepository, AccountGroupInfoService accountGroupInfoService, AccountService accountService, ContactService contactService, GroupService groupService) {
        this.expenseRepository = expenseRepository;
        this.accountGroupInfoService = accountGroupInfoService;
        this.accountService = accountService;
        this.contactService = contactService;
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

    public Set<Expense> getExpensesForAccountByLender(Integer accountId, Integer lenderId) {
        return expenseRepository.findAll().stream()
                .filter(s -> s.getBorrowerId().equals(accountId) && s.getLenderId().equals(lenderId)
                        && s.getExpenseType() == ExpenseType.USER)
                .collect(Collectors.toSet());
    }

    public Set<Expense> getExpensesForGroupByLender(Integer groupId, Integer lenderId) {
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

    public void exportToExcel(List<Expense> expenses) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Expense");

        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        Cell cell = row.createCell(0);
        cell.setCellValue("#");
        cell = row.createCell(1);
        cell.setCellValue("Date and Time");
        cell = row.createCell(2);
        cell.setCellValue("Amount");
        cell = row.createCell(3);
        cell.setCellValue("Currency");
        cell = row.createCell(4);
        cell.setCellValue("Lender Name");
        cell = row.createCell(5);
        cell.setCellValue("Borrower Name");

        for (Expense expense : expenses) {
            row = sheet.createRow(rownum++);
            cell = row.createCell(0);
            cell.setCellValue(expense.getId());
            cell = row.createCell(1);
            cell.setCellValue(expense.getLocalDateTime().toString());
            cell = row.createCell(2);
            cell.setCellValue(expense.getAmount());
            cell = row.createCell(3);
            cell.setCellValue(expense.getCurrency().name());
            cell = row.createCell(4);
            Account account = accountService.get(expense.getLenderId());
            Contact contact = contactService.get(account.getTelephoneNumber());
            cell.setCellValue(contact.getName());
            cell = row.createCell(5);
            if (expense.getExpenseType() == USER) {
                account = accountService.get(expense.getBorrowerId());
                contact = contactService.get(account.getTelephoneNumber());
                cell.setCellValue(contact.getName());
            } else {
                Group group = groupService.get(expense.getBorrowerId());
                cell.setCellValue(group.getName());
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File("expense.xlsx"));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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