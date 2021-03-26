package com.parking.lot.util;

import com.parking.lot.helper.NormalStaff;
import com.parking.lot.helper.ParkingStaff;
import com.parking.lot.helper.SmartStaff;

public class GetParkingHelper {

  public static ParkingStaff randomGetParkingHelper() {
    if (Math.random() > 0.5) {
      return new SmartStaff();
    } else {
      return new NormalStaff();
    }
  }
}
