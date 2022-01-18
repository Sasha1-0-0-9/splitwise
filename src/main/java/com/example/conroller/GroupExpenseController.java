package com.example.conroller;

import com.example.entity.Expense;
import com.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/accounts/{accountId}/groups/{groupId}/expenses")
public class GroupExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public GroupExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping()
    public String getAll(@PathVariable("accountId") Integer accountId,
                         @PathVariable("groupId") Integer groupId, Model model) {
        List<Expense> expenses = expenseService.getExpensesByGroup(groupId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("expenses", expenses);
        model.addAttribute("text", null);
        return "groupExpenses/index";
    }

    @GetMapping("/excel")
    public String exportToExcel(@PathVariable("accountId") Integer accountId,
                                @PathVariable("groupId") Integer groupId, Model model) {
        List<Expense> expenses = expenseService.getExpensesByGroup(groupId);
        model.addAttribute("accountId", accountId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("expenses", expenses);
        expenseService.exportToExcel(expenses);
        model.addAttribute("text", "The file is saved in the root of the project");
        return "groupExpenses/index";
    }
}