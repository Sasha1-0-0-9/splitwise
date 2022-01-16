package com.example.conroller;

import com.example.entity.*;
import com.example.exception.AccountNotFoundException;
import com.example.service.AccountService;
import com.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/accounts/{accountId}/expenses")
public class AccountExpenseController {

    private final ExpenseService expenseService;
    private final AccountService accountService;

    @Autowired
    public AccountExpenseController(ExpenseService expenseService, AccountService accountService) {
        this.expenseService = expenseService;
        this.accountService = accountService;
    }

    @GetMapping()
    public String getAll(@PathVariable("accountId") Integer accountId, Model model) {
        List<Expense> expenses = expenseService.getAllAccountExpense(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("expenses", expenses);
        model.addAttribute("text", null);
        return "expenses/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") Integer id, Model model) {
        Expense expense = expenseService.get(id);
        model.addAttribute("expense", expense);
        Account lender = accountService.get(expense.getLenderId());
        model.addAttribute("lender", lender);
        model.addAttribute("name", expenseService.getExpenseName(expense));
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
            expenseService.create(amount, currency, expenseType, lenderName, borrowerName, accountId);
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
        expenseService.exportToExcel(expenses);
        model.addAttribute("text", "The file is saved in the root of the project");
        return "expenses/index";
    }
}
