package com.parking.lot.util;

import com.parking.lot.entity.Storage;

import java.util.ArrayList;
import java.util.List;

public class StorageUtil {

    public static List<Storage> generateStorageList(int size, String basementId) {
        List<Storage> storageList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Storage storage = Storage.builder().id(GenerateId.getUUID())
                    .address(String.valueOf(i)).carId(null).basementId(basementId).build();

            storageList.add(storage);
        }
        return storageList;
    }
}
