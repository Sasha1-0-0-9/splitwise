package com.example.common.validator;

import com.example.entity.Group;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class GroupCreationValidator implements Predicate<Group> {

    @Override
    public boolean test(Group group) {
        if (group == null) {
            return false;
        }

        if (group.getCreatorId() == null) {
            return false;
        }

        if (group.getName() == null) {
            return false;
        }

        if (group.getName().isBlank()) {
            return false;
        }

        return true;
    }
}
