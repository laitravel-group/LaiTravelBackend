package com.laitravel.laitravelbe.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {
    // String -> date time types

    // Convert a date string of YYYY-MM-DD to Timestamp
    public static Timestamp dateStringToTimestamp(String dateString) {
        try {
            LocalDate localDate = LocalDate.parse(dateString);
            LocalDateTime localDateTime = localDate.atTime(6, 0); // Assuming time as 6:00
            return Timestamp.valueOf(localDateTime);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            throw new IllegalArgumentException("Invalid date format. Expected format: YYYY-MM-DD", e);
        }
    }

    // Convert a time string of HH:mm to LocalTime
    public static LocalTime timeStringToLocalTime(String timeString) {
        try {
            return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            // Handle invalid time format
            throw new IllegalArgumentException("Invalid time format. Expected format: HH:mm", e);
        }
    }

    // Convert a date string of YYYY-MM-DD to LocalDate
    public static LocalDate dateStringToLocalDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd", e);
        }
    }


    // date time types to string

    // Convert a Timestamp of date to date string of YYYY-MM-DD
    public static String dateTimestampToString(Timestamp date) {
        return date.toLocalDateTime().toLocalDate().toString();
    }

    // Convert a LocalTime of time to time string of HH:mm
    public static String localTimeToString(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String localDateToString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


}
