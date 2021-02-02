package com.parking.lot.util;

import com.parking.lot.entity.helper.NormalHelper;
import com.parking.lot.entity.helper.ParkingHelper;
import com.parking.lot.entity.helper.SmartHelper;

public class GetParkingHelper {

  public static ParkingHelper randomGetParkingHelper() {
    if (Math.random() > 0.5) {
      return new SmartHelper();
    } else {
      return new NormalHelper();
    }
  }
}
