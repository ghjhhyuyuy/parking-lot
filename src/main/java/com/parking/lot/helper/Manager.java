package com.parking.lot.helper;

import com.parking.lot.entity.Parking;
import com.parking.lot.util.GetParkingHelper;
import java.util.List;

public class Manager extends ParkingHelper {

  ParkingHelper parkingHelper;

  public Manager() {
    this.parkingHelper = GetParkingHelper.randomGetParkingHelper();
  }

  @Override
  public Parking parking(List<Parking> parkings) {
    return parkingHelper.parking(parkings);
  }
}
