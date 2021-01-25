package com.parking.lot.util;

import java.time.LocalDateTime;

public class TimeUtil {

  public static LocalDateTime getTime(int hour) {
    LocalDateTime localDateTime = LocalDateTime.now();
    return localDateTime.plusHours(hour);
  }
}
