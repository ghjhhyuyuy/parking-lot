package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class IllegalTicketException extends Throwable {

  private final ExceptionMessage exceptionMessage;

  public IllegalTicketException(ExceptionMessage exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  public ExceptionMessage getExceptionMessage() {
    return this.exceptionMessage;
  }
}
