package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Expense;
import com.example.entity.Group;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/accounts/expenses")
public class AccountExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseExportService expenseExportService;
    private final AccountService accountService;
    private final GroupService groupService;
    private final ContactService contactService;

    @Autowired
    public AccountExpenseController(ExpenseService expenseService,
                                    ExpenseExportService expenseExportService, AccountService accountService, GroupService groupService, ContactService contactService) {
        this.expenseService = expenseService;
        this.expenseExportService = expenseExportService;
        this.accountService = accountService;
        this.groupService = groupService;
        this.contactService = contactService;
    }


    @GetMapping()
    public ModelAndView getAll(Authentication authentication) {
        ModelAndView model = new ModelAndView("expenses/index");
        Account account = accountService.getByEmail(authentication.getName());
        List<Expense> expenses = expenseService.getAllAccountExpense(account.getId());
        model.addObject("expenses", expenses);
        return model;
    }

    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") Integer id) {
        ModelAndView model = new ModelAndView("expenses/show");
        Expense expense = expenseService.get(id);
        model.addObject("expense", expense);
        Account lender = accountService.get(expense.getLenderId());
        model.addObject("lender", lender);
        model.addObject("name", expenseService.getExpenseName(expense));
        return model;
    }

    @GetMapping("/new")
    public ModelAndView newExpense(Authentication authentication) {
        ModelAndView model = new ModelAndView("expenses/new");
        Account account = accountService.getByEmail(authentication.getName());
        List<Group> groups = groupService.getAllByAccountId(account.getId());
        model.addObject("phoneNumber", account.getTelephoneNumber());
        model.addObject("contacts", contactService.getByAccountId(account.getId()));
        model.addObject("groups", groups);

        return model;
    }

    @PostMapping("/new")
    public ModelAndView createExpense(@RequestParam("amount") String amount, @RequestParam("currency") String currency,
                                      @RequestParam("expenseType") String expenseType, @RequestParam("lenderName") String lenderName,
                                      @RequestParam("borrowerName") String borrowerName,
                                      Authentication authentication) {
        ModelAndView model = new ModelAndView("expenses/index");
        Account account = accountService.getByEmail(authentication.getName());
        expenseService.create(amount, currency, expenseType, lenderName, borrowerName, account.getId());
        List<Expense> expenses = expenseService.getAllAccountExpense(account.getId());
        model.addObject("expenses", expenses);
        return model;
    }

    @GetMapping("/excel")
    public ResponseEntity<Resource> exportToExcel(Authentication authentication) {
        Account account = accountService.getByEmail(authentication.getName());
        List<Expense> expenses = expenseService.getAllAccountExpense(account.getId());

        byte[] data = expenseExportService.exportToExcel(expenses);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(data.length)
                .header("Content-disposition", "inline; filename=" + "file.xlsx")
                .body(new ByteArrayResource(data));
    }
}
