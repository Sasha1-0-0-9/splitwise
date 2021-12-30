package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

public class AccountGroupInfo implements Serializable {

    private final Integer accountId;
    private final Integer groupId;
    private final AccountRole accountRole;

    public AccountGroupInfo(Integer accountId, Integer groupId, AccountRole accountRole) {
        this.accountId = accountId;
        this.groupId = groupId;
        this.accountRole = accountRole;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public AccountRole getAccountRole() {
        return accountRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountGroupInfo that = (AccountGroupInfo) o;
        return accountId.equals(that.accountId) &&
                groupId.equals(that.groupId) &&
                accountRole == that.accountRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, groupId, accountRole);
    }
}
