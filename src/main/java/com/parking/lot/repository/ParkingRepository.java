package com.parking.lot.repository;

import com.parking.lot.entity.Parking;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends CrudRepository<Parking, String> {

  @Override
  List<Parking> findAll();
}
