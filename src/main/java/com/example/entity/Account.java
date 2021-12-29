package com.example.entity;

import com.example.common.Identifiable;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Identifiable, Serializable {

    private Integer accountId;
    private final User user;

    public Account(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Integer getId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) &&
                Objects.equals(getUser(), account.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, getUser());
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userName=" + user.getName() +
                '}';
    }
}