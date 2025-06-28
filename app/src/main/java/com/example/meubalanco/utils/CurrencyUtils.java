package com.example.meubalanco.utils;

import androidx.core.net.ParseException;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    // Formata números para R$ 1.234,56
    public static String formatToBRL(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return format.format(value);
    }

    // Converte "R$ 1.234,56" para double
    public static double parseBRL(String currencyValue) {
        try {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            return format.parse(currencyValue.replace("R$", "")).doubleValue();
        } catch (ParseException | java.text.ParseException e) {
            return 0.0;
        }
    }
}