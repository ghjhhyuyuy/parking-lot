package com.parking.lot.util;

import com.parking.lot.staff.NormalStaff;
import com.parking.lot.staff.ParkingStaff;
import com.parking.lot.staff.SmartStaff;

public class GetParkingStaff {

  public static ParkingStaff randomGetParkingStaff() {
    if (Math.random() > 0.5) {
      return new SmartStaff();
    } else {
      return new NormalStaff();
    }
  }
}
