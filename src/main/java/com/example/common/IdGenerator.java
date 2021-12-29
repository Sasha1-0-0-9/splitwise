package com.example.common;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private final static AtomicInteger accountCounter = new AtomicInteger(0);
    private static int groupCounter = 0;
    private static int accountGroupInfoCounter = 0;
    private static int expenseCounter = 0;

    public static Integer getAccountCounter() {
        return accountCounter.incrementAndGet();
    }

    public static Integer getGroupCounter() {
        return ++groupCounter;
    }

    public static Integer getAccountGroupInfoCounter() {
        return ++accountGroupInfoCounter;
    }

    public static Integer getExpenseCounter() {
        return ++expenseCounter;
    }
}