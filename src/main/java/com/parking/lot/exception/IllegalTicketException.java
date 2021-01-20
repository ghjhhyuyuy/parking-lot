package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class IllegalTicketException extends MyException {

  public IllegalTicketException(ExceptionMessage exceptionMessage) {
    super(exceptionMessage);
  }
}
