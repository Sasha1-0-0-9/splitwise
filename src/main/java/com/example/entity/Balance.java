package com.example.entity;

import java.util.Objects;

public class Balance {

    private final double amount;
    private final Currency currency;

    public Balance(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Balance)) return false;
        Balance balance = (Balance) o;
        return Double.compare(balance.getAmount(), getAmount()) == 0 &&
                getCurrency() == balance.getCurrency();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getCurrency());
    }
}
