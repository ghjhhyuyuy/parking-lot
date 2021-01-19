package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotFoundResourceException extends Throwable {

  private final ExceptionMessage exceptionMessage;

  public NotFoundResourceException(ExceptionMessage exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  public ExceptionMessage getExceptionMessage() {
    return this.exceptionMessage;
  }
}
