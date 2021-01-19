package com.parking.lot.repository;

import com.parking.lot.entity.Parking;
import org.springframework.data.repository.CrudRepository;

public interface ParkingRepository extends CrudRepository<Parking, String> {

}
