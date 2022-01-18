package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AccountRepo extends JpaRepository<Account, Integer> {

    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Account getAccByName(@Param("email") String email);

    @Query("SELECT a FROM Account a WHERE a.telephoneNumber = :telephoneNumber")
    Account getByTelephoneNumber(@Param("telephoneNumber") String telephoneNumber);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.email = :email WHERE a.id = :id")
    void update(@Param("email") String email, @Param("id") int id);

   /* @Query("SELECT a FROM Account a WHERE a.telephoneNumber = :telephoneNumber AND a.encodedPassword = :encodedPAssword")
    public Account getByNameAndPassword(@Param("telephoneNumber") String telephoneNumber,
                                        @Param("encodedPassword") String encodedPassword);*/
}
