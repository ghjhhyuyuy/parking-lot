package com.parking.lot.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StorageUtil {

  private final List<String> zoneList = Arrays.asList("A", "B", "C");
  private final Random random = new Random();

  public String getZone() {
    int num = random.nextInt(3);
    return zoneList.get(num);

  }

  public String getAddress() {
    int num = random.nextInt(10);
    return String.valueOf(num);
  }
}
