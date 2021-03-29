package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotManagerException extends MyException {

  public NotManagerException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
