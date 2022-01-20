package com.example.service;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.entity.Expense;
import com.example.entity.Group;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.example.entity.ExpenseType.USER;

@Service
public class ExpenseExportService {

    private final AccountService accountService;
    private final ContactService contactService;
    private final GroupService groupService;

    public ExpenseExportService(AccountService accountService, ContactService contactService, GroupService groupService) {
        this.accountService = accountService;
        this.contactService = contactService;
        this.groupService = groupService;
    }

    public byte[] exportToExcel(List<Expense> expenses) {
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

        byte[] bytes = new byte[0];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            bytes = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
