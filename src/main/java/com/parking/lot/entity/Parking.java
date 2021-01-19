package com.parking.lot.entity;

import com.parking.lot.exception.OverSizeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Parking {
  String id;
  int size;

  public void reduceSize() {
    this.size--;
  }
  public void upSize() {
    this.size++;
  }
  public void checkSize() {
    if (this.size > 0) {
      return;
    }
    throw new OverSizeException();
  }
}
