package com.example.repository;

import com.example.entity.AccountGroupInfo;
import com.example.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountGroupInfoRepo extends JpaRepository<AccountGroupInfo, Integer> {

    @Query("SELECT agi FROM AccountGroupInfo agi WHERE agi.accountId = :accountId " +
            "AND agi.groupId = :groupId")
    public AccountGroupInfo getByAccountAndGroupId(@Param("accountId") int accountId,
                                                   @Param("groupId") int groupId);

    @Query("SELECT agi FROM AccountGroupInfo agi WHERE agi.accountId = :accountId ")
    public List<AccountGroupInfo> getByAccountId(@Param("accountId") int accountId);

    @Query("SELECT agi FROM AccountGroupInfo agi WHERE agi.groupId = :groupId")
    public List<AccountGroupInfo> getByGroupId(@Param("groupId") int groupId);
}
