package com.example.repository;

import com.example.entity.Account;
import com.example.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Contact contact) {
        jdbcTemplate.update("INSERT INTO Contacts VALUES(?, ?, ?)", contact.getTelephoneNumber(),
                contact.getName(), contact.getEmail());
    }

    public List<Contact> getAll() {
        return jdbcTemplate.query("SELECT * FROM Contacts", new BeanPropertyRowMapper<>(Contact.class));
    }

    public Contact get(String telephoneNumber) {
        List<Contact> contacts = jdbcTemplate.query("SELECT * FROM Contacts WHERE telephoneNumber=?",
                new BeanPropertyRowMapper<>(Contact.class), telephoneNumber);
        return contacts.stream().findAny().orElse(null);
    }

    public void update(int id, Contact updatedContact) {
        jdbcTemplate.update("UPDATE Contacts SET name=?, telephoneNumber=?, email=? WHERE id=?", updatedContact.getName(),
                updatedContact.getTelephoneNumber(), updatedContact.getEmail(), id);
    }

    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM Account WHERE id=?", id);

    }

    public List<Contact> getByAccountId(Integer accountId) {
        return jdbcTemplate.query("SELECT * FROM contacts AS c"
                + " INNER JOIN account_contact_list AS a ON a.telephonenumber = c.telephonenumber"
                + " WHERE a.accountId = ? ", new BeanPropertyRowMapper<>(Contact.class), accountId);
    }

    public Account getAccount(String telephoneNumber) {
        List<Account> accounts = jdbcTemplate.query("SELECT * FROM accounts WHERE telephoneNumber=?",
                new BeanPropertyRowMapper<>(Account.class), telephoneNumber);
        return accounts.stream().findAny().orElse(null);
    }
}