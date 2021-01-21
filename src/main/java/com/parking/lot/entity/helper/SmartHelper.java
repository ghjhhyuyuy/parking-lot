package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SmartHelper extends ParkingHelper {


  @Override
  public Parking parking(List<Parking> parkings) {
    Optional<Parking> optionalParking = getMaxSetsParking(parkings);
    if (optionalParking.isPresent()) {
      return optionalParking.get();
    } else {
      throw new OutOfSetException(ExceptionMessage.OUT_OF_SET);
    }
  }

  private Optional<Parking> getMaxSetsParking(List<Parking> parkings) {
    return parkings.stream().filter(parking -> parking.getSize() > 0)
        .max(Comparator.comparingInt(Parking::getSize));
  }

}
