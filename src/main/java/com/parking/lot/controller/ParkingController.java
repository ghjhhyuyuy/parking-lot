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
    Ticket ticket = parkingService.parkingCarBySelf(parkingId, car);
    return new Result<Ticket>().getSuccessResult(ticket);
  }

  @PostMapping("/{id}")
  public Result<Car> takeCar(@PathVariable("id") String ticketId, String willTakeCarId)
      throws NotFoundResourceException {
    Car canTake = parkingService.takeCar(ticketId, willTakeCarId);
    return new Result<Car>().getSuccessResult(canTake);
  }
}
