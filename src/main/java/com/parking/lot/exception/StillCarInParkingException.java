package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class StillCarInParkingException extends MyException {

  public StillCarInParkingException() {
    super(ExceptionMessage.STILL_CAR_IN_PARKING);
  }
}
