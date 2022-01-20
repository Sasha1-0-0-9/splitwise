package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "account_group_info")
public class AccountGroupInfo {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "group_id")
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
}
