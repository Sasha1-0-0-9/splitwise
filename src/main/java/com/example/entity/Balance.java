package com.example.entity;

public class Balance {

    private final double amount;
    private final Currency currency;

    public Balance(double amount, Currency currency) {
        this.amount = round(amount);
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    private static double round(double value) {
        double scale = Math.pow(10, 3);
        return Math.round(value * scale) / scale;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
