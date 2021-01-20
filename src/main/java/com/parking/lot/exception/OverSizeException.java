package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class OverSizeException extends MyException {

  public OverSizeException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
