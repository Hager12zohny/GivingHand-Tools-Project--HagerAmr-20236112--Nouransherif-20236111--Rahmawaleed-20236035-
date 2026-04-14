package com.givinghand.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidBirthday(String birthday) {
        if (birthday == null || birthday.isBlank()) return false;
        try {
            LocalDate date = LocalDate.parse(birthday, DateTimeFormatter.ISO_LOCAL_DATE);
            //  person >= 5 years old
            return date.isBefore(LocalDate.now().minusYears(5));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidRole(String role) {
        return "donor".equalsIgnoreCase(role) || "organization".equalsIgnoreCase(role);
    }
}