package com.parking.lot.controller;

import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helper")
public class HelperController {

  private final ParkingService parkingService;

  @Autowired
  public HelperController(
      ParkingService parkingService) {
    this.parkingService = parkingService;
  }

  @GetMapping("/{id}")
  public Result<Ticket> helperSave(@PathVariable("id") String userId)
      throws NotFoundResourceException {
    return new Result<Ticket>().getSuccessResult(parkingService.helperSave(userId, false));
  }
}
