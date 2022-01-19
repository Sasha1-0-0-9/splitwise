package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name ="account_contact_list")
public class AccountContactList {

    @Id
    @Column(name = "accountid", nullable = false)
    private Integer id;

    @Column(name = "contact")
    private String contact;

    public AccountContactList() {
    }

    public AccountContactList(Integer id, String contact) {
        this.id = id;
        this.contact = contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
