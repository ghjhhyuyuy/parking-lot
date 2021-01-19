package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class OverSizeException extends RuntimeException {

  public OverSizeException() {
    super(ExceptionMessage.PARKING_OVER_SIZE.getMessage());
  }
}
