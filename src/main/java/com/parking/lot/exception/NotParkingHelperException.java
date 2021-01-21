package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotParkingHelperException extends MyException {

  public NotParkingHelperException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
