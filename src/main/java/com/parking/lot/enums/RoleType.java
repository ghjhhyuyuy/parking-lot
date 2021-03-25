package com.parking.lot.enums;

import com.parking.lot.helper.Manager;
import com.parking.lot.helper.NormalHelper;
import com.parking.lot.helper.ParkingHelper;
import com.parking.lot.helper.SmartHelper;

public enum RoleType {
  NORMAL_HELPER(new NormalHelper(),"1"),
  SMART_HELPER(new SmartHelper(),"2"),
  MANGER(new Manager(),"3");
  private final ParkingHelper parkingHelper;
  private final String id;

  RoleType(ParkingHelper parkingHelper,String id) {
    this.parkingHelper = parkingHelper;
    this.id = id;
  }

  public ParkingHelper getParkingHelper() {
    return this.parkingHelper;
  }

  public String getId() {
    return this.id;
  }
}
