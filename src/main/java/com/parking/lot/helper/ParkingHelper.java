package com.parking.lot.helper;

import com.parking.lot.entity.Parking;
import java.util.List;
import lombok.Getter;

@Getter
public abstract class ParkingHelper {

  final boolean isAllow = true;

  public abstract Parking parking(List<Parking> parkings);
}
