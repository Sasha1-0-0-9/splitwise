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

        if (account.getUser() == null) {
            return false;
        }

        return true;
    }
}
