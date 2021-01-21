package com.parking.lot.enums;

import com.parking.lot.entity.helper.Manager;
import com.parking.lot.entity.helper.NormalHelper;
import com.parking.lot.entity.helper.ParkingHelper;
import com.parking.lot.entity.helper.SmartHelper;

public enum RoleType {
  NORMAL_HELPER(new NormalHelper()),
  SMART_HELPER(new SmartHelper()),
  MANGER(new Manager());
  private final ParkingHelper parkingHelper;

  RoleType(ParkingHelper parkingHelper) {
    this.parkingHelper = parkingHelper;
  }

  public ParkingHelper getParkingHelper() {
    return this.parkingHelper;
  }

}
