package com.parking.lot.entity;

import com.parking.lot.exception.OverSizeException;
import java.util.concurrent.atomic.AtomicInteger;
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

  public AtomicInteger getAtomicIntegerSize() {
    return new AtomicInteger(this.size);
  }

  public void reduceSize() {
    this.size = getAtomicIntegerSize().decrementAndGet();
  }

  public void upSize() {
    this.size = getAtomicIntegerSize().incrementAndGet();
  }

  public void checkSize() {
    if (this.size > 0) {
      return;
    }
    throw new OverSizeException();
  }
}
