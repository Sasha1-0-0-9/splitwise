package com.example.ss;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Acc {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "encryptedpassword")
    private String encryptedPassword;

    public Acc() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public Acc(String email, String phoneNumber, String encryptedPassword) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.encryptedPassword = encryptedPassword;
    }
}
