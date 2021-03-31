package com.parking.lot.staff;

import com.parking.lot.entity.Basement;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.Comparator;
import java.util.List;

public class SmartStaff extends ParkingStaff {


  @Override
  public Basement parking(List<Basement> basements) {
    return basements.stream().filter(parking -> parking.getStorageList().size() > 0)
            .max(Comparator.comparingInt(Basement::getEmptyNumber))
            .orElseThrow(() -> new OutOfSetException(ExceptionMessage.OUT_OF_SET));
  }

}
