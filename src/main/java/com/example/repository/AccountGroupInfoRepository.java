package com.example.repository;

import com.example.entity.Account;
import com.example.entity.AccountGroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.common.IdGenerator.getAccountGroupInfoCounter;

@Repository
public class AccountGroupInfoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountGroupInfoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(AccountGroupInfo accountGroupInfo) {
        jdbcTemplate.update("INSERT INTO account_group_info (accountId, groupId, accountRole) VALUES(?, ?, ?)",
                accountGroupInfo.getAccountId(), accountGroupInfo.getGroupId(), accountGroupInfo.getAccountRole().name());
    }

    public Map<Integer, AccountGroupInfo> getAll() {
        return null;
    }

    public AccountGroupInfo get(Integer accountId, Integer groupId) {
        List<AccountGroupInfo> accounts = jdbcTemplate.query("SELECT * FROM account_group_info WHERE accountId=? AND groupId=?",
                new BeanPropertyRowMapper<>(AccountGroupInfo.class), accountId, groupId);
        return accounts.stream().findAny().orElse(null);
    }

    public List<AccountGroupInfo> getByAccountId(Integer id) {
        List<AccountGroupInfo> list = jdbcTemplate.query("SELECT * FROM account_group_info WHERE accountId=?",
                new BeanPropertyRowMapper<>(AccountGroupInfo.class), id);
        return list;
    }

    public void delete(AccountGroupInfo accountGroupInfo) {
        jdbcTemplate.update("DELETE FROM account_group_info WHERE accountId=? AND groupId=?",
                accountGroupInfo.getAccountId(), accountGroupInfo.getGroupId());
    }

    public List<AccountGroupInfo> getByGroupId(Integer id) {
        return jdbcTemplate.query("SELECT * FROM account_group_info WHERE groupId=?",
                new BeanPropertyRowMapper<>(AccountGroupInfo.class), id);
    }
}