package com.example.entity;

import javax.validation.constraints.*;

public class Account {
    private Integer id;

    @NotEmpty(message = "Telephone number should not be empty")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String telephoneNumber;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 8, max = 30, message = "Name should be between 8 and 30 characters")
    private String encodedPassword;

    public Account() {}

    public Account(String telephoneNumber, String encodedPassword) {
        this.telephoneNumber = telephoneNumber;
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
}