package com.parking.lot.enums;

import com.parking.lot.staff.Manager;
import com.parking.lot.staff.NormalStaff;
import com.parking.lot.staff.ParkingStaff;
import com.parking.lot.staff.SmartStaff;

public enum RoleType {
  NORMAL_STAFF(new NormalStaff(),"1"),
  SMART_STAFF(new SmartStaff(),"2"),
  MANGER(new Manager(),"3");
  private final ParkingStaff parkingStaff;
  private final String id;

  RoleType(ParkingStaff parkingStaff, String id) {
    this.parkingStaff = parkingStaff;
    this.id = id;
  }

  public ParkingStaff getParkingStaff() {
    return this.parkingStaff;
  }

  public String getId() {
    return this.id;
  }
}
