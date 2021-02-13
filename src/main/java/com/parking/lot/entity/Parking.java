package com.parking.lot.entity;

import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.IllegalSizeException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.util.GenerateID;
import com.parking.lot.util.StorageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Parking {

  @Id
  private String id;
  private int size;
  @OneToMany(mappedBy = "parkingId", cascade = {CascadeType.MERGE})
  @NotFound(action = NotFoundAction.IGNORE)
  private List<Storage> storageList;

  public static Parking createParking(int size) {
    if (size > 0) {
      String id = GenerateID.getUUID();
      List<Storage> storageList = generateStorageList(size, id);
      return new Parking(GenerateID.getUUID(), size, storageList);
    }
    throw new IllegalSizeException(ExceptionMessage.ILLEGAL_SIZE);
  }

  private static List<Storage> generateStorageList(int size, String parkingId) {
    StorageUtil storageUtil = new StorageUtil();
    List<Storage> storageList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Storage storage = Storage.builder().id(GenerateID.getUUID()).zone(storageUtil.getZone())
          .address(storageUtil.getAddress()).carId(null).parkingId(parkingId).build();
      storageList.add(storage);
    }
    return storageList;
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
    if (this.storageList.size() > 0) {
      return;
    }
    throw new OverSizeException(ExceptionMessage.PARKING_OVER_SIZE);
  }
}
