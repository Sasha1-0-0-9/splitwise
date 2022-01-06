package com.example.entity;

public class AccountContactList {

    private Integer accountId;
    private String telephoneNumber;

    public AccountContactList() {}

    public AccountContactList(Integer accountId, String telephoneNumber) {
        this.accountId = accountId;
        this.telephoneNumber = telephoneNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }
}
