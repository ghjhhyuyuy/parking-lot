package com.parking.lot.util;

import java.util.UUID;

public class GenerateID {

  public static String getUUID() {
    return UUID.randomUUID().toString();
  }
}
