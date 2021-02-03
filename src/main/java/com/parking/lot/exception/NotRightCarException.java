package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class NotRightCarException extends MyException {

  public NotRightCarException() {
    super(ExceptionMessage.NOT_RIGHT_CAR);
  }
}
