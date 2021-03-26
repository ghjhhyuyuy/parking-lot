package com.parking.lot.helper;

import com.parking.lot.entity.Basement;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OutOfSetException;
import java.util.List;

public class NormalStaff extends ParkingStaff {

  @Override
  public Basement parking(List<Basement> basements) {
    return basements.stream().filter(parking -> parking.getStorageList().size() > 0).findFirst()
            .orElseThrow(() -> new OutOfSetException(ExceptionMessage.OUT_OF_SET));
  }

}
