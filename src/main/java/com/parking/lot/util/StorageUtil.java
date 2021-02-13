package com.parking.lot.util;

import com.parking.lot.entity.Storage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StorageUtil {

  private final List<String> zoneList = Arrays.asList("A", "B", "C");
  private final Random random = new Random();

  public String getZone() {
    int num = random.nextInt(3);
    return zoneList.get(num);

  }

  public String getAddress() {
    int num = random.nextInt(10);
    return String.valueOf(num);
  }

  public static List<Storage> generateStorageList(int size, String parkingId) {
    StorageUtil storageUtil = new StorageUtil();
    List<Storage> storageList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Storage storage = Storage.builder().id(GenerateID.getUUID()).zone(storageUtil.getZone())
          .address(storageUtil.getAddress()).carId(null).parkingId(parkingId).build();
      storageList.add(storage);
    }
    return storageList;
  }
}
