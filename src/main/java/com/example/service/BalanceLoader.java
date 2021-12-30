package com.example.service;

import com.example.entity.Balance;
import com.example.entity.Currency;

import java.time.LocalDateTime;
import java.util.Map;

public interface BalanceLoader {

    Balance getAccountBalance(Integer groupId, Integer accountId);

    Balance getAccountBalance(Integer accountId);

    Balance getAccountBalance(Integer groupId, Integer accountId, LocalDateTime afterDate);

    Balance getAccountBalance(Integer accountId, LocalDateTime afterDate);

    Map<Currency, Balance> getAccountBalanceByCurrencies(Integer groupId, Integer accountId);

    Map<Currency, Balance> getAccountBalanceByCurrencies(Integer accountId);
}
