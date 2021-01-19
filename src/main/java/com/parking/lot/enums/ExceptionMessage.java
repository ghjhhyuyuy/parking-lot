package com.parking.lot.enums;

public enum ExceptionMessage {
  NOT_FOUND_PARKING("not found parking"),
  NOT_FOUND_TICKET("not found ticket"),
  ILLEGAL_TICKET("illegal ticket"),
  PARKING_OVER_SIZE("parking over size");

  private final String message;

  ExceptionMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
