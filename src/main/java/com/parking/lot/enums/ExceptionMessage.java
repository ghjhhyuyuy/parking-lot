package com.parking.lot.enums;

public enum ExceptionMessage {
  NOT_FOUND_PARKING("603", "not found parking"),
  NOT_FOUND_TICKET("604", "not found ticket"),
  NOT_FOUND_USER("606", "not found user"),
  ILLEGAL_TICKET("601", "illegal ticket"),
  NOT_PARKING_HELPER("605", "Not ParkingHelper"),
  PARKING_OVER_SIZE("602", "parking over size"),
  NO_MATCHING_ROLE("607", "no matching role"),
  OUT_OF_SET("608", "there is no set in all parkings"),
  ILLEGAL_SIZE("609","illegal size"),
  NOT_MANAGER_USER("610","not manager user");
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
