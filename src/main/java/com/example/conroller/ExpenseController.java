package com.example.conroller;

import com.example.entity.*;
import com.example.exception.AccountNotFoundException;
import com.example.service.AccountService;
import com.example.service.ContactService;
import com.example.service.ExpenseService;
import com.example.service.GroupService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.entity.ExpenseType.USER;

@Controller
@RequestMapping("/accounts/{accountId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final AccountService accountService;
    private final ContactService contactService;
    private final GroupService groupService;

    @Autowired
    public ExpenseController(ExpenseService expenseService, AccountService accountService, ContactService contactService, GroupService groupService) {
        this.expenseService = expenseService;
        this.accountService = accountService;
        this.contactService = contactService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String getAll(@PathVariable("accountId") Integer accountId, Model model) {
        List<Expense> expenses = expenseService.getAllAccountExpense(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("expenses", expenses);
        return "expenses/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("accountId") Integer accountId, @PathVariable("id") Integer id, Model model) {
        Expense expense = expenseService.get(id);
        model.addAttribute("expense", expense);

        Account lender = accountService.get(expense.getLenderId());
        model.addAttribute("lender", lender);

        String name;
        if (expense.getExpenseType() == USER) {
            Account account = accountService.get(expense.getBorrowerId());
            //Contact contact = contactService.get(account.getTelephoneNumber());
            name = account.getTelephoneNumber();
        } else {
            Group group = groupService.get(expense.getBorrowerId());
            name = group.getName();
        }

        model.addAttribute("name", name);

        return "expenses/show";
    }

    @GetMapping("/new")
    public String newExpense(@PathVariable("accountId") Integer accountId, Model model) {
        model.addAttribute("accountId", accountId);
        model.addAttribute("text", null);
        return "expenses/new";
    }

    @PostMapping("/new")
    public String createExpense(@RequestParam("amount") String amount, @RequestParam("currency") String currency,
                                @RequestParam("expenseType") String expenseType, @RequestParam("lenderName") String lenderName,
                                @RequestParam("borrowerName") String borrowerName,
                                @PathVariable("accountId") Integer accountId, Model model) {
        model.addAttribute("accountId", accountId);

        try {
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
            expenseService.save(expense);
        } catch (ConstraintViolationException | AccountNotFoundException | NumberFormatException e) {
            String message = e.getMessage();
            model.addAttribute("text", message);
            return "expenses/new";
        }

        List<Expense> expenses = expenseService.getAllAccountExpense(accountId);
        model.addAttribute("expenses", expenses);
        return "expenses/index";
    }

    @GetMapping("/excel")
    public String exportToExcel(@PathVariable("accountId") Integer accountId, Model model) {
        List<Expense> expenses = expenseService.getAllAccountExpense(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("expenses", expenses);

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

        return "expenses/index";
    }
}
