package com.parking.lot.util;

import java.util.UUID;

public class GenerateId {
//todo lock
  public static String getUUID() {
    return UUID.randomUUID().toString();
  }
}
