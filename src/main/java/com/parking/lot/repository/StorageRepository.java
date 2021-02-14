package com.parking.lot.repository;

import com.parking.lot.entity.Storage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends CrudRepository<Storage, String> {

  Storage findByParkingId(String parkingId);

}
