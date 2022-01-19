package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AccountRepo extends JpaRepository<Account, Integer> {

    Account findAccountByEmail(String email);

    Account findAccountByTelephoneNumber(String telephoneNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.email = :email WHERE a.id = :id")
    void update(@Param("email") String email, @Param("id") int id);

}
