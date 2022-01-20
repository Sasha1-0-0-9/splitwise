package com.example.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phone_number")
    @NotEmpty(message = "Telephone number should not be empty")
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String telephoneNumber;

    @Column(name = "encrypted_password")
    @NotEmpty(message = "Password should not be empty")
    private String encodedPassword;

    public Account() {
    }

    public Account(String email, String telephoneNumber, String encodedPassword) {
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.encodedPassword = encodedPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }
}