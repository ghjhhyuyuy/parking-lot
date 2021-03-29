package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotParkingStaffException extends MyException {

  public NotParkingStaffException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
