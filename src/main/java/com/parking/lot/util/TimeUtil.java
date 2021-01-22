package com.parking.lot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

  public static String getTime(int ms) {
    Date date = new Date(System.currentTimeMillis() + ms);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return simpleDateFormat.format(date);
  }
}
