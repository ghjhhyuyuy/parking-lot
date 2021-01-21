package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import java.util.List;

public class Manager extends ParkingHelper {

  @Override
  public Parking parking(List<Parking> parkings, boolean byOrderForManager) {
    if (byOrderForManager) {
      return new NormalHelper().parking(parkings, true);
    } else {
      return new SmartHelper().parking(parkings, false);
    }
  }
}
