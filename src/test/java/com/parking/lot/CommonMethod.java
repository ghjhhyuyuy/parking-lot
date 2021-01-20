package com.parking.lot;

import com.parking.lot.entity.Parking;

public class CommonMethod {
  public static Parking getParking() {
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").size(20).build();
  }
}
