package com.example.repository;

import com.example.entity.Account;
import com.example.entity.Contact;
import com.example.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Group group) {
        jdbcTemplate.update("INSERT INTO groups (name, creatorId) VALUES(?, ?)", group.getName(), group.getCreatorId());
        return getByNameAndPassword(group.getName(), group.getCreatorId()).getId();
    }

    public Group getByNameAndPassword(String name, int creatorId) {
        List<Group> groups = jdbcTemplate.query("SELECT * FROM groups WHERE name=? AND creatorId=?",
                new BeanPropertyRowMapper<>(Group.class), name, creatorId);
        return groups.stream().findAny().orElse(null);
    }

    public List<Group> getAll() {
        return jdbcTemplate.query("SELECT * FROM groups", new BeanPropertyRowMapper<>(Group.class));
    }

    public List<Group> getAllByAccountId(int accountId) {
        List<Group> list = jdbcTemplate.query("SELECT * FROM groups AS g"
                + " INNER JOIN account_group_info AS a_g_i ON a_g_i.GroupId = g.Id"
                + " INNER JOIN accounts AS a ON a.Id = a_g_i.AccountId"
                + " WHERE a.Id = ? ", new BeanPropertyRowMapper<>(Group.class), accountId);
        return list;
    }

    public Group get(int id) {
        return jdbcTemplate.query("SELECT * FROM groups WHERE id=?", new BeanPropertyRowMapper<>(Group.class), id)
                .stream().findAny().orElse(null);
    }

    public void update(int id, Group updatedGroup) {
        jdbcTemplate.update("UPDATE groups SET name=? WHERE id=?", updatedGroup.getName(), id);
    }

    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM groups WHERE id=?", id);

    }

    public List<Contact> getContactsByGroupId(Integer id) {
        return jdbcTemplate.query("SELECT * FROM contacts AS c"
                + " INNER JOIN accounts AS a ON a.telephonenumber = c.telephonenumber"
                + " INNER JOIN account_group_info AS a_g_i ON a_g_i.accountid = a.id"
                + " INNER JOIN groups AS g ON g.id = a_g_i.groupid"
                + " WHERE g.id = ? ", new BeanPropertyRowMapper<>(Contact.class), id);
    }
}