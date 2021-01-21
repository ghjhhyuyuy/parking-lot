package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NoMatchingRoleException extends MyException {

  public NoMatchingRoleException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
