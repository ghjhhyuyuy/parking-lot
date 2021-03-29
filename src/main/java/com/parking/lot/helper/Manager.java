package com.parking.lot.helper;

import com.parking.lot.entity.Basement;
import com.parking.lot.util.GetParkingHelper;
import java.util.List;

public class Manager extends ParkingStaff {

  ParkingStaff parkingStaff;

  public Manager() {
    this.parkingStaff = GetParkingHelper.randomGetParkingHelper();
  }

  @Override
  public Basement parking(List<Basement> basements) {
    return parkingStaff.parking(basements);
  }
}
