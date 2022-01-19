package com.example.repository;

import com.example.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepo extends JpaRepository<Contact, String> {

   /* @Query("SELECT a FROM AccountContacts a"
            + " WHERE a.id= :id")
    List<Contact> getByAccountId(@Param("id") int id);*/
}
