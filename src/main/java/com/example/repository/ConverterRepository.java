package com.example.repository;

import com.example.entity.Currency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConverterRepository {

    private final static String fileName = "src/main/resources/currency_rates.txt";

    public static double converter(Currency fromCurrency, Currency toCurrency) {
        Map<Currency, Double> converterUSDTo = get();
        System.out.println(converterUSDTo.isEmpty());
        return (converterUSDTo.isEmpty()) ? 0 : round(converterUSDTo.get(toCurrency) / converterUSDTo.get(fromCurrency));
    }

    private static Map<Currency, Double> get() {
        String lastLine = null;
        String firstLine;

        Map<Currency, Double> converterUSDTo = new EnumMap<>(Currency.class);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            firstLine = reader.readLine();
            String text;
            while ((text = reader.readLine()) != null) {
                lastLine = text;
            }

            if (firstLine == null || lastLine == null || firstLine.isBlank() || lastLine.isBlank()) {
                return converterUSDTo;
            }

            List<Currency> collect = Arrays.stream(firstLine.split(";"))
                    .skip(1)
                    .map(Currency::valueOf)
                    .collect(Collectors.toList());

            List<Double> value = Arrays.stream(lastLine.split(";"))
                    .skip(1)
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());

            converterUSDTo.put(Currency.USD, 1.0);
            for (int i = 0; i < collect.size(); i++) {
                converterUSDTo.put(collect.get(i), value.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return converterUSDTo;
    }

    private static double round(double value) {
        double scale = Math.pow(10, 3);
        return Math.round(value * scale) / scale;
    }
}