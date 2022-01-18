package com.example.repository;

import com.example.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

   /* @Query(value = "insert into expenses (lenderid, borrowerid, amount, time, currency, expensetype) VALUES ( :lenderid," +
            " :borrowerid, :amount, :time, :currency, :expensetype", nativeQuery = true)
    void saveExpense(@Param("lenderid") int lenderid, @Param("borrowerid") int borrowerid,
                             @Param("amount") double amount, @Param("time") Timestamp time,
                             @Param("currency") Currency currency, @Param("expensetype") ExpenseType expensetype);*/
}
