package com.example.entity;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private final String name;
    private final String telephoneNumber;
    private final String email;

    public User(String name, String telephoneNumber, String email) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getName(), user.getName()) &&
                Objects.equals(telephoneNumber, user.telephoneNumber) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), telephoneNumber, email);
    }
}
