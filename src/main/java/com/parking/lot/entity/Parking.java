package com.parking.lot.entity;

import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.IllegalSizeException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.util.GenerateID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Parking {

  @Id
  private String id;
  private int size;

  public static Parking createParking(int size) {
    if(size>0){
      return new Parking(GenerateID.getUUID(), size);
    }
    throw new IllegalSizeException(ExceptionMessage.ILLEGAL_SIZE);
  }

  public AtomicInteger getAtomicIntegerSize() {
    return new AtomicInteger(this.size);
  }

  public void reduceSize() {
    this.size = getAtomicIntegerSize().decrementAndGet();
  }

  public void upSize() {
    this.size = getAtomicIntegerSize().incrementAndGet();
  }

  public void ifSizeMoreThanZero() {
    if (this.size > 0) {
      return;
    }
    throw new OverSizeException(ExceptionMessage.PARKING_OVER_SIZE);
  }
}
