package com.example.entity;

import java.util.Objects;

public class Group {

    private Integer id;
    private String name;
    private Integer creatorId;

    public Group() {}

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

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) &&
                Objects.equals(getName(), group.getName()) &&
                Objects.equals(getCreatorId(), group.getCreatorId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getCreatorId());
    }
}