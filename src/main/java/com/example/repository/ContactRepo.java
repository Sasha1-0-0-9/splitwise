package com.example.repository;

import com.example.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact, String> {

    Contact findContactByPhoneNumber(String phoneNumber);
}
