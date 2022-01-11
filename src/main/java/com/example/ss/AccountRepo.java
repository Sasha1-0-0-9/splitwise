package com.example.ss;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepo extends JpaRepository<Account, Integer> {

    @Query("SELECT a FROM Account a WHERE a.email = :email")
    public Account getAccByName(@Param("email") String email);

    @Query("SELECT a FROM Account a WHERE a.telephoneNumber = :telephoneNumber")
    public Account getByTelephoneNumber(@Param("telephoneNumber") String telephoneNumber);

   /* @Query("SELECT a FROM Account a WHERE a.telephoneNumber = :telephoneNumber AND a.encodedPassword = :encodedPAssword")
    public Account getByNameAndPassword(@Param("telephoneNumber") String telephoneNumber,
                                        @Param("encodedPassword") String encodedPassword);*/
}
