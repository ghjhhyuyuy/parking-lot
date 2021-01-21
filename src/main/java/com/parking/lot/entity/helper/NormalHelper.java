package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalHelper extends ParkingHelper {

  AtomicInteger index = new AtomicInteger(0);

  @Override
  public Parking parking(List<Parking> parkings, boolean byOrderForManager) {
    if (parkings.size() <= index.get()) {
      resetIndex();
      throw new OutOfSetException(ExceptionMessage.OUT_OF_SET);
    }
    Parking parking = parkings.get(index.get());
    if (parking.getSize() > 0) {
      resetIndex();
      return parking;
    }
    index.incrementAndGet();
    return parking(parkings, byOrderForManager);
  }

  private void resetIndex() {
    this.index = new AtomicInteger(0);
  }
}
