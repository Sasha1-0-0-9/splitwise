package com.example.repository;

import com.example.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepo extends JpaRepository<Group, Integer> {

    @Query("SELECT g FROM Group g INNER JOIN AccountGroupInfo agi ON agi.groupId = g.id INNER JOIN Account a ON a.id = agi.accountId WHERE a.id = :accountId")
    List<Group> getAllByAccountId(@Param("accountId") int accountId);

    Group findGroupById(int id);
}
