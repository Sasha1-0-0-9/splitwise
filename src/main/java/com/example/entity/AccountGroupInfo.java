package com.example.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account_group_info")
public class AccountGroupInfo {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accountid")
    private Integer accountId;

    @Column(name = "groupid")
    private Integer groupId;

    public AccountGroupInfo() {
    }

    public AccountGroupInfo(Integer accountId, Integer groupId) {
        this.accountId = accountId;
        this.groupId = groupId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
