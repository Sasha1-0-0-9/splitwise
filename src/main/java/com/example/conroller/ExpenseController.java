package com.example.conroller;

import com.example.entity.*;
import com.example.service.AccountService;
import com.example.service.ContactService;
import com.example.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<Expense> expenses = expenseService.getExpensesByAccount(accountId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("expenses", expenses);
        return "expenses/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("accountId") Integer accountId, @PathVariable("id") Integer id, Model model) {
        Expense expense = expenseService.get(id);
        model.addAttribute("expense", expense);

        String name;
        if (expense.getExpenseType() == ExpenseType.USER) {
            Account account = accountService.get(expense.getBorrowerId());
            Contact contact = contactService.get(account.getTelephoneNumber());
            name = contact.getName();
        } else {
            Group group = groupService.get(expense.getBorrowerId());
            name = group.getName();
        }

        model.addAttribute("name", name);

        return "expenses/show";
    }

    @GetMapping("/new")
    public String newExpense(@PathVariable("accountId") Integer accountId, Model model) {
        return "groups/new";
    }
}
