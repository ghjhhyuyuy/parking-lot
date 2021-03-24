package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.Comparator;
import java.util.List;

public class SmartHelper extends ParkingHelper {


  @Override
  public Parking parking(List<Parking> parkings) {
    return getMaxSetsParking(parkings);
  }

  private Parking getMaxSetsParking(List<Parking> parkings) {
    return parkings.stream().filter(parking -> parking.getStorageList().size() > 0)
        .max(Comparator.comparingInt(Parking::getEmptyNumber))
        .orElseThrow(() -> new OutOfSetException(ExceptionMessage.OUT_OF_SET));
  }

}
