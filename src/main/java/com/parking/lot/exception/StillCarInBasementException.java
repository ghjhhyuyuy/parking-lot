package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class StillCarInBasementException extends MyException {

  public StillCarInBasementException() {
    super(ExceptionMessage.STILL_CAR_IN_BASEMENT);
  }
}
