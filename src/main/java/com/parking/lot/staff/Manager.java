package com.parking.lot.staff;

import com.parking.lot.entity.Basement;
import com.parking.lot.util.GetParkingStaff;

import java.util.List;

public class Manager extends ParkingStaff {

    @Override
    public Basement parking(List<Basement> basements) {
        return GetParkingStaff.randomGetParkingStaff().parking(basements);
    }
}
