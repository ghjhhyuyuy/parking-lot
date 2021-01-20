package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotPartingHelperException extends MyException {

  public NotPartingHelperException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
