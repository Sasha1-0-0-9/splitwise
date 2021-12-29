package com.example.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Account {
    private Integer accountId;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private final String name;

    @NotEmpty(message = "Telephone number should not be empty")
    @Pattern(regexp="(^$|[0-9]{10})")
    private final String telephoneNumber;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private final String email;

    public Account(User user, String name, String telephoneNumber, String email) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}