package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotManagerUserException extends MyException {

  public NotManagerUserException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
