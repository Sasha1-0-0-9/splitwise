package com.example.repository;

import com.example.entity.Currency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConverterRepository {

    private final static String fileName = "src/main/resources/currency_rates.txt";

    public static double converter(Currency fromCurrency, Currency toCurrency) {
        String lastLine = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String text;
            while ((text = reader.readLine()) != null) {
                lastLine = text;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (fromCurrency) {
            case USD: {
                switch (toCurrency) {
                    case EUR: {
                        return Double.parseDouble(lastLine.split(";")[1]);
                    }
                    case UAH: {
                        return Double.parseDouble(lastLine.split(";")[2]);
                    }
                    case USD: {
                        return 1.0;
                    }
                }
            }

            case EUR: {
                switch (toCurrency) {
                    case EUR: {
                        return 1.0;
                    }
                    case UAH: {
                        return round(1.0 / Double.parseDouble(lastLine.split(";")[1])
                                * Double.parseDouble(lastLine.split(";")[2]));
                    }
                    case USD: {
                        return round(1.0 / Double.parseDouble(lastLine.split(";")[1]));
                    }
                }
            }

            case UAH: {
                switch (toCurrency) {
                    case EUR: {
                        return round(1.0 / Double.parseDouble(lastLine.split(";")[2])
                                * Double.parseDouble(lastLine.split(";")[1]));
                    }
                    case UAH: {
                        return 1;
                    }
                    case USD: {
                        return round(1.0 / Double.parseDouble(lastLine.split(";")[2]));
                    }
                }
            }
        }

        return 0;
    }

    private static double round(double value) {
        double scale = Math.pow(10, 2);
        return Math.round(value * scale) / scale;
    }
}