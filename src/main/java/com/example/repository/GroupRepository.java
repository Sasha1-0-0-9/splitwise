package com.example.repository;

import com.example.entity.Group;

import java.util.Map;
import java.util.Set;

import static com.example.common.IdGenerator.getGroupCounter;

@org.springframework.stereotype.Repository
public class GroupRepository implements Repository<Group> {

    private final static String FILE_NAME = "groups.txt";

    @Override
    public void save(Group group) {
        int newGroupId = getGroupCounter();
        group.setGroupId(newGroupId);
        RepositoryTemplate.save(newGroupId, group, FILE_NAME);
    }

    @Override
    public Map<Integer, Group> getAll() {
        return RepositoryTemplate.getItems(FILE_NAME);
    }

    @Override
    public void deleteAll(Set<Integer> ids) {
        RepositoryTemplate.deleteAll(ids, FILE_NAME);
    }

    @Override
    public int getSize() {
        return RepositoryTemplate.getSize(FILE_NAME);
    }
}