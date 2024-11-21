package com.consultify.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
