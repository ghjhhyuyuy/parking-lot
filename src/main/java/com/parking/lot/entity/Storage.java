package com.parking.lot.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Storage {

  @Id
  private String id;
  private String address;
  private String carId;
  private String parkingId;

  public void removeCarId() {
    this.carId = null;
  }

  public void parkingCar(String id) {
    this.carId = id;
  }
}
