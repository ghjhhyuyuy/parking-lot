package com.parking.lot.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.locks.ReentrantLock;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Storage {
    @Id
    private String id;
    private String address;
    @Setter
    private String carId;
    @Setter
    private String basementId;

    public void removeCarId() {
        this.carId = null;
    }
}
