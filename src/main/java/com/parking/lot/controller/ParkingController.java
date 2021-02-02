package com.parking.lot.controller;

import com.parking.lot.entity.Car;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parking")
public class ParkingController {

  private final ParkingService parkingService;

  @Autowired
  public ParkingController(
      ParkingService parkingService) {
    this.parkingService = parkingService;
  }

  @GetMapping("/{id}")
  public Result<Ticket> parkingCarBySelf(@PathVariable("id") String parkingId, Car car)
      throws NotFoundResourceException {
    return new Result<Ticket>().getSuccessResult(parkingService.parkingCarBySelf(parkingId, car));
  }

  @PostMapping("/{id}")
  public Result<Car> takeCar(@PathVariable("id") String ticketId)
      throws NotFoundResourceException {
    return new Result<Car>().getSuccessResult(parkingService.takeCar(ticketId));
  }
}
