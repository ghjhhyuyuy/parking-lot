package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.List;

public class NormalHelper extends ParkingHelper {

  @Override
  public Parking parking(List<Parking> parkings) {
    return getParkingByOrder(parkings);
  }

  private Parking getParkingByOrder(List<Parking> parkings) {
    return parkings.stream().filter(parking -> parking.getStorageList().size() > 0).findFirst()
        .orElseThrow(() -> new OutOfSetException(ExceptionMessage.OUT_OF_SET));
  }

}
