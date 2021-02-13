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
  private String zone;
  private String address;
  private String carId;
  private String parkingId;

  public static Storage saveCarInStorage(Parking parking, Car car) {
    Storage storage = parking.getStorageList().get(0);
    storage.carId = car.getId();
    parking.getStorageList().remove(0);
    return storage;
  }

  public void removeCarId() {
    this.carId = null;
  }
}
