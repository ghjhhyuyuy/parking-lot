package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class MyException extends RuntimeException {

  private final ExceptionMessage exceptionMessage;

  public MyException(ExceptionMessage exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  public ExceptionMessage getExceptionMessage() {
    return this.exceptionMessage;
  }
}
