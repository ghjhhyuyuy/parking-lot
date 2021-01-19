package com.parking.lot.controller;

import com.parking.lot.entity.Ticket;
import com.parking.lot.service.ParkingService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParkingController {

  private final ParkingService parkingService;

  @Autowired
  public ParkingController(
      ParkingService parkingService) {
    this.parkingService = parkingService;
  }

  @GetMapping("/ticket/{id}")
  public Ticket parkingCar(@PathVariable("id") String parkingId) throws NotFoundException {
    return parkingService.parkingCar(parkingId);
  }
}
