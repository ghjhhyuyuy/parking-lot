package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class OverSizeException extends RuntimeException {

  private final ExceptionMessage exceptionMessage;

  public OverSizeException(ExceptionMessage exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  public ExceptionMessage getExceptionMessage() {
    return this.exceptionMessage;
  }
}
