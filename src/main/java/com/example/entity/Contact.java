package com.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "contacts")
public class Contact {

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @Id
    @Column(name = "phonenumber")
    @NotEmpty(message = "Telephone number should not be empty")
    @Pattern(regexp = "(^$|[0-9]{10})")
    private String telephoneNumber;

    @Column(name = "balance")
    private Double balance;

    public Contact(String name, String telephoneNumber) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.balance = 0.0;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return telephoneNumber.equals(contact.telephoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(telephoneNumber);
    }
}
