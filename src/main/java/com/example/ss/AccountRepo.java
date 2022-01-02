package com.example.ss;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepo extends JpaRepository<Acc, Long> {

    @Query("SELECT a FROM Acc a WHERE a.email = :email")
    public Acc getAccByName(@Param("email") String email);
}
