package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParkingHelper {

  final boolean isAllow = true;

  public Parking parking(List<Parking> parkings, boolean byOrderForManager) {
    System.out.println("not implementation in here");
    return null;
  }
}
