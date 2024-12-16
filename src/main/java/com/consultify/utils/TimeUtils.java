package com.consultify.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
  public static String getCurrentTimeInISO() {
    TimeZone tz = TimeZone.getTimeZone("Asia/Kuala_Lumpur");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    df.setTimeZone(tz);
    return df.format(new Date());
  }

  public static Date convertISOToDate(String timeInISO) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(timeInISO);
    } catch (Exception e) {
      return null;
    }
  }

  public static Boolean isPast(String timeInISO) {
    Date time = convertISOToDate(timeInISO);
    return new Date().after(time);
  }

  public static Boolean isBefore(String timeInISO) {
    Date time = convertISOToDate(timeInISO);
    return new Date().before(time);
  }

  public static String getTime(String timeInISO) {
    Date time = convertISOToDate(timeInISO);
    return new SimpleDateFormat("HH:mm").format(time);
  }

  public static String getDate(String timeInISO) {
    Date time = convertISOToDate(timeInISO);
    return new SimpleDateFormat("dd/MM/yyyy").format(time);
  }

  public static LocalDate parseDate(String date) {
    if (date == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return LocalDate.parse(date, formatter);
  }

  public static String formatDate(LocalDate date) {
    if (date == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return date.format(formatter);
  }

  public static boolean inBetween(String date, String startDate, String endDate) {
    if (date == null || startDate == null || endDate == null) {
      return false;
    }

    LocalDate parsedDate = parseDate(date);
    LocalDate parsedStartDate = parseDate(startDate);
    LocalDate parsedEndDate = parseDate(endDate);

    return !parsedDate.isBefore(parsedStartDate) && !parsedDate.isAfter(parsedEndDate);
  }

  public static Long daysFromNow(String date) {
    try {
      Date parsedDate = convertISOToDate(date);
      Date now = new Date();
      Instant parsedInstant = parsedDate.toInstant();
      Instant nowInstant = now.toInstant();

      long days = ChronoUnit.DAYS.between(nowInstant, parsedInstant);
      return days;
    } catch (Exception e) {
      return null;
    }
  }
}
