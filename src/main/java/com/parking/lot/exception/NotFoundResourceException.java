package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotFoundResourceException extends MyException {

  public NotFoundResourceException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
