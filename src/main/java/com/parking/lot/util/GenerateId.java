package com.parking.lot.util;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class GenerateId {
    private static final ReentrantLock reentrantLock = new ReentrantLock();

    public static String getUUID() {
        reentrantLock.lock();
        String id = UUID.randomUUID().toString();
        reentrantLock.unlock();
        return id;
    }
}
