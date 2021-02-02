package com.parking.lot.entity;

import com.parking.lot.util.GenerateID;
import com.parking.lot.util.StorageUtil;
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

  public static Storage getStorage(Car car) {
    StorageUtil storageUtil = new StorageUtil();
    return Storage.builder().id(GenerateID.getUUID()).zone(storageUtil.getZone())
        .address(storageUtil.getAddress()).carId(car.getId()).build();
  }
}
