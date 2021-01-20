package com.parking.lot.enums;

public enum InstanseType {
  NORMAL_HELPER("1"),
  SMART_HELPER("2"),
  MANGER("3");
  private final String id;

  InstanseType(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

}
