package com.example.service;

import com.example.common.SortType;

import java.time.LocalDateTime;

public interface ExpenseHistoryPrinter {

    void printAccountExpenseHistory(Integer accountId, SortType sortType);

    void printGroupExpenseHistory(Integer groupId, SortType sortType);

    void printAccountExpenseHistoryGroupByCurrencies(Integer accountId, SortType sortType);

    void printGroupExpenseHistoryGroupByCurrencies(Integer groupId, SortType sortType);

    void printAccountExpenseHistoryGroupByCurrencies(Integer accountId, SortType sortType, LocalDateTime afterDate);

    void printGroupExpenseHistoryGroupByCurrencies(Integer groupId, SortType sortType, LocalDateTime afterDate);
}