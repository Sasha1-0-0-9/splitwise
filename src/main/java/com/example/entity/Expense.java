package com.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Expense implements Serializable {

    private Integer id;
    private Integer lenderId;
    private Integer borrowerId;
    private double amount;
    private ExpenseType expenseType;
    private LocalDateTime localDateTime;
    private Currency currency;

    public Integer getId() {
        return id;
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

    public LocalDateTime getLocalDateTime() {
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

    public void setLocalDateTime(LocalDateTime localDateTime) {
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

        public ExpenseBuilder setLocalDateTime(LocalDateTime localDateTime) {
            expense.setLocalDateTime(localDateTime);
            return this;
        }

        public ExpenseBuilder setCurrency(Currency currency) {
            expense.setCurrency(currency);
            return this;
        }

        public Expense build() {
            return expense;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(lenderId, expense.lenderId) &&
                Objects.equals(amount, expense.amount) &&
                expenseType == expense.expenseType &&
                Objects.equals(localDateTime, expense.localDateTime) &&
                currency == expense.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lenderId, amount, expenseType, localDateTime, currency);
    }
}