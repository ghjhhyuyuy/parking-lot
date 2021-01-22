package com.parking.lot.controller;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.User;
import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.service.ParkingService;
import java.text.ParseException;
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

  @GetMapping("/{id}")
  public Result<Ticket> helperSave(@PathVariable("id") String userId, boolean byOrderForManager)
      throws NotFoundResourceException {
    return new Result<Ticket>()
        .getSuccessResult(parkingService.helperSave(userId, byOrderForManager));
  }

  @PostMapping("/parking")
  public Result<Parking> addParking(int size)
      throws NotFoundResourceException{
    return new Result<Parking>().getSuccessResult(parkingService.addParking(size));
  }

  @PostMapping("/user")
  public Result<User> addUser(String name,String role)
      throws NotFoundResourceException{
    return new Result<User>().getSuccessResult(parkingService.addUser(name,role));
  }

  @DeleteMapping("/user/{id}")
  public Result<User> deleteUser(@PathVariable("id") String userId)
      throws NotFoundResourceException{
    return new Result<User>().getSuccessResult(parkingService.removeUser(userId));
  }

  @DeleteMapping("/parking/{id}")
  public Result<Parking> deleteParking(@PathVariable("id") String parkingId)
      throws NotFoundResourceException{
    parkingService.removeParking(parkingId);
    return new Result<Parking>().getSuccessResult(null);
  }
}
