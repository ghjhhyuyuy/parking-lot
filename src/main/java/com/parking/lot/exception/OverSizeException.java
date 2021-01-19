package com.parking.lot.exception;

public class OverSizeException extends RuntimeException {

  public OverSizeException() {
    super("parking over size");
  }
}
