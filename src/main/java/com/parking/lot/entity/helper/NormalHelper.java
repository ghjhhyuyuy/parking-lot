package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NormalHelper extends ParkingHelper{
  AtomicInteger index = new AtomicInteger(0);
  @Override
  public void parking(List<Parking> parkings) {
    Parking parking = parkings.get(index.get());
    if(parking.getSize()>0 || parkings.size() <= index.get()){
      parking.reduceSize();
      return;
    }
    index.incrementAndGet();
    parking(parkings);
  }
}
