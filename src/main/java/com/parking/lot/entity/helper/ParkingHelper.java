package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public abstract class ParkingHelper {

  final boolean isAllow = true;

  public abstract Parking parking(List<Parking> parkings);
}
