package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class OutOfSetException extends MyException {

  public OutOfSetException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
