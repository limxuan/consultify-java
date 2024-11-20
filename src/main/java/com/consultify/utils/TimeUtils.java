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
}
