package com.example.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "lender_id")
    @NotNull(message = "An account with this name does not exist")
    private Integer lenderId;

    @Column(name = "borrower_id")
    @NotNull(message = "An account/group with this name does not exist")
    private Integer borrowerId;

    @Column(name = "amount")
    @Positive(message = "Amount should be more than 0")
    private double amount;

    @Column(name = "time")
    private Timestamp localDateTime;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "expense_type")
    private ExpenseType expenseType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLenderId() {
        return lenderId;
    }

    public Integer getBorrowerId() {
        return borrowerId;
    }

    public double getAmount() {
        return amount;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public Timestamp getLocalDateTime() {
        return localDateTime;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setLenderId(Integer lenderId) {
        this.lenderId = lenderId;
    }

    public void setBorrowerId(Integer borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public void setLocalDateTime(Timestamp localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public static class ExpenseBuilder {

        private final Expense expense;

        public ExpenseBuilder() {
            expense = new Expense();
        }

        public ExpenseBuilder setBorrowerId(Integer borrowerId) {
            expense.borrowerId = borrowerId;
            return this;
        }

        public ExpenseBuilder setLenderId(Integer lenderId) {
            expense.setLenderId(lenderId);
            return this;
        }

        public ExpenseBuilder setAmount(double amount) {
            expense.setAmount(amount);
            return this;
        }

        public ExpenseBuilder setExpenseType(ExpenseType expenseType) {
            expense.setExpenseType(expenseType);
            return this;
        }

        public ExpenseBuilder setLocalDateTime(Timestamp localDateTime) {
            expense.setLocalDateTime(localDateTime);
            return this;
        }

        public ExpenseBuilder setCurrency(Currency currency) {
            expense.setCurrency(currency);
            return this;
        }

        public ExpenseBuilder setId(int id) {
            expense.setId(expense.id);
            return this;
        }

        public Expense build() {
            return expense;
        }
    }
}