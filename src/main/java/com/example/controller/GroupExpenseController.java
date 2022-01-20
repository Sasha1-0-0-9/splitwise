package com.example.controller;

import com.example.entity.Expense;
import com.example.service.ExpenseExportService;
import com.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/accounts/groups/{groupId}/expenses")
public class GroupExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseExportService expenseExportService;

    @Autowired
    public GroupExpenseController(ExpenseService expenseService, ExpenseExportService expenseExportService) {
        this.expenseService = expenseService;
        this.expenseExportService = expenseExportService;
    }

    @GetMapping()
    public ModelAndView getAll(@PathVariable("groupId") Integer groupId) {
        ModelAndView model = new ModelAndView("groupExpenses/index");
        List<Expense> expenses = expenseService.getExpensesByGroup(groupId);
        model.addObject("expenses", expenses);
        model.addObject("groupId", groupId);
        return model;
    }

    @GetMapping("/excel")
    public ResponseEntity<Resource> exportToExcel(@PathVariable("groupId") Integer groupId) {
        List<Expense> expenses = expenseService.getExpensesByGroup(groupId);
        byte[] data = expenseExportService.exportToExcel(expenses);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(data.length)
                .header("Content-disposition", "inline; filename=" + "file.xlsx")
                .body(new ByteArrayResource(data));
    }
}