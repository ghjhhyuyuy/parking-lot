package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalHelper extends ParkingHelper {

  AtomicInteger index = new AtomicInteger(0);

  @Override
  public Parking parking(List<Parking> parkings) {
    if (parkings.size() <= index.get()) {
      throw new OutOfSetException(ExceptionMessage.OUT_OF_SET);
    }
    Parking parking = parkings.get(index.get());
    if (parking.getSize() > 0) {
      return parking;
    }
    index.incrementAndGet();
    return parking(parkings);
  }
}
