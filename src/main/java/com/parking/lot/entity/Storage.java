package com.parking.lot.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    private String parkingId;

    public void removeCarId() {
        this.carId = null;
    }
}
