package com.parking.lot.entity.helper;

import com.parking.lot.entity.Parking;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SmartHelper extends ParkingHelper{
  @Override
  public void parking(List<Parking> parkings) {
    Optional<Parking> optionalParking = parkings.stream().max(Comparator.comparingInt(Parking::getSize));
    if(optionalParking.isPresent()){
      Parking parking = optionalParking.get();
      parking.reduceSize();
    }
  }

}
