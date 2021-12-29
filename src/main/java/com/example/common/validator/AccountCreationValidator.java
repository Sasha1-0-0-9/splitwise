package com.example.common.validator;

import com.example.entity.Account;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class AccountCreationValidator implements Predicate<Account> {

    @Override
    public boolean test(Account account) {
        if (account == null) {
            return false;
        }

        if (account.getName() == null || account.getName().isBlank()) {
            return false;
        }

        if (account.getTelephoneNumber() == null || account.getTelephoneNumber().isBlank()) {
            return false;
        }

        if (account.getEmail() == null || account.getEmail().isBlank()) {
            return false;
        }

        return true;
    }
}
