package com.parking.lot.enums;

public enum ExceptionMessage {
  NOT_FOUND_BASEMENT("603", "Not found basement"),
  NOT_FOUND_TICKET("604", "Not found ticket"),
  NOT_FOUND_STAFF("606", "Not found staff"),
  ILLEGAL_TICKET("601", "Illegal ticket"),
  NOT_ACTIVE_STAFF("605", "Not active staff"),
  BASEMENT_OVER_SIZE("602", "Basement over size"),
  NO_MATCHING_ROLE("607", "No matching role"),
  OUT_OF_SET("608", "There is no set in all basements"),
  ILLEGAL_SIZE("609", "Illegal size"),
  NOT_MANAGER("610", "Not manager"),
  NOT_FOUND_CAR("611", "Not found car"),
  NOT_FOUND_STORAGE("612", "Not found storage"),
  NOT_RIGHT_CAR("613", "Not right car"),
  STILL_CAR_IN_BASEMENT("614", "Can not remove basement because still car in basement");
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
