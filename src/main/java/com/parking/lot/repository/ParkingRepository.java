package com.parking.lot.repository;

import com.parking.lot.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface ParkingRepository extends CrudRepository<Ticket, Integer> {

}
