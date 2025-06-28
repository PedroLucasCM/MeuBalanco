package com.example.meubalanco.utils;

import androidx.core.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ValidationUtils {
    // Valida se um valor é positivo
    public static boolean isPositive(double value) {
        return value > 0;
    }

    // Valida datas (ex: dd/MM/yyyy)
    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("pt", Locale.forLanguageTag("BR"));
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }
}