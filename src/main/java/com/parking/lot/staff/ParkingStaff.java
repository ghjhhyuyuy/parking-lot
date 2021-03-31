package com.parking.lot.staff;

import com.parking.lot.entity.Basement;

import java.util.List;
import lombok.Getter;

@Getter
public abstract class ParkingStaff {

  public abstract Basement parking(List<Basement> basements);
}
