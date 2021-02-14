package com.parking.lot.controller;

import com.parking.lot.entity.Car;
import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.User;
import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController {

  private final ParkingService parkingService;

  @Autowired
  public ManagerController(
      ParkingService parkingService) {
    this.parkingService = parkingService;
  }

  @PostMapping("/{id}")
  public Result<Ticket> parkingCarByHelper(@PathVariable("id") String userId,
      Car car)
      throws NotFoundResourceException {
    Ticket ticket = parkingService.parkingCarByHelper(userId, car);
    return new Result<Ticket>().getSuccessResult(ticket);
  }

  @PostMapping("/parking")
  public Result<Parking> addParking(String id, int size)
      throws NotFoundResourceException {
    Parking parking = parkingService.addParking(size);
    return new Result<Parking>().getSuccessResult(parking);
  }

  @PostMapping("/user")
  public Result<User> addUser(String id, String name, String role)
      throws NotFoundResourceException {
    User user = parkingService.addUser(name, role);
    return new Result<User>().getSuccessResult(user);
  }

  @DeleteMapping("/user/{id}")
  public Result<User> deleteUser(@PathVariable("id") String userId)
      throws NotFoundResourceException {
    User user = parkingService.removeUser(userId);
    return new Result<User>().getSuccessResult(user);
  }

  @DeleteMapping("/parking/{id}")
  public Result<Parking> deleteParking(String id,
      @PathVariable("id") String parkingId)
      throws NotFoundResourceException {
    parkingService.removeParking(parkingId);
    return new Result<Parking>().getSuccessResult(null);
  }
}
