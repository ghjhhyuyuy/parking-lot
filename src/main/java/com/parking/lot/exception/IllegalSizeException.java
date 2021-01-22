package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class IllegalSizeException extends MyException {

  public IllegalSizeException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
