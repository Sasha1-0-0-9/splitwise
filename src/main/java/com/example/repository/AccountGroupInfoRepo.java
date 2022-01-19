package com.example.repository;

import com.example.entity.AccountGroupInfo;
import com.example.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountGroupInfoRepo extends JpaRepository<AccountGroupInfo, Integer> {

    AccountGroupInfo findAccountGroupInfoByAccountIdAndGroupId(int accountId, int groupId);

    List<AccountGroupInfo> findAccountGroupInfosByAccountId(int accountId);

    List<AccountGroupInfo> findAccountGroupInfosByGroupId(int groupId);
}
