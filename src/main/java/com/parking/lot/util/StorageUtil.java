package com.parking.lot.util;

import com.parking.lot.entity.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageUtil {

    private final Random random = new Random();


    private String getAddress() {
        int num = random.nextInt(10);
        return String.valueOf(num);
    }

    public static List<Storage> generateStorageList(int size, String parkingId) {
        StorageUtil storageUtil = new StorageUtil();
        List<Storage> storageList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Storage storage = Storage.builder().id(GenerateId.getUUID())
                    .address(storageUtil.getAddress()).carId(null).parkingId(parkingId).build();
            storageList.add(storage);
        }
        return storageList;
    }
}
