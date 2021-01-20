package com.parking.lot.enums;

public enum ExceptionMessage {
  NOT_FOUND_PARKING("603", "not found parking"),
  NOT_FOUND_TICKET("604", "not found ticket"),
  ILLEGAL_TICKET("601", "illegal ticket"),
  PARKING_OVER_SIZE("602", "parking over size");
  private final String code;
  private final String message;

  ExceptionMessage(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }

  public String getCode() {
    return this.code;
  }
}