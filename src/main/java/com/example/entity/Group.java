package com.example.entity;

import com.example.common.Identifiable;

import java.io.Serializable;
import java.util.Objects;

public class Group implements Identifiable, Serializable {

    private Integer groupId;
    private final String name;
    private final Integer creatorId;

    public Group(String name, Integer creatorId) {
        this.name = name;
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    @Override
    public Integer getId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return Objects.equals(groupId, group.groupId) &&
                Objects.equals(getName(), group.getName()) &&
                Objects.equals(getCreatorId(), group.getCreatorId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, getName(), getCreatorId());
    }
}