package com.parking.lot.controller;

import com.parking.lot.entity.Car;
import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.vo.Result;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(
            ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/self/{id}")
    public Result<Ticket> parkingCarBySelf(@PathVariable("id") String parkingId, Car car)
            throws NotFoundResourceException {
        Ticket ticket = parkingService.parkingCarBySelf(parkingId, car);
        return new Result<Ticket>().getSuccessResult(ticket);
    }

    @PostMapping("/staff/{id}")
    public Result<Ticket> parkingCarByStaff(@PathVariable("id") String userId,
                                            Car car)
            throws NotFoundResourceException {
        Ticket ticket = parkingService.parkingCarByStaff(userId, car);
        return new Result<Ticket>().getSuccessResult(ticket);
    }

    @GetMapping("/{id}")
    public Result<Car> takeCar(@PathVariable("id") String ticketId, String willTakeCarId)
            throws NotFoundResourceException {
        Car canTake = parkingService.takeCar(ticketId, willTakeCarId);
        return new Result<Car>().getSuccessResult(canTake);
    }

    @PostMapping("/parking")
    public Result<Parking> addParking(String id, int size)
            throws NotFoundResourceException {
        Parking parking = parkingService.addParking(size);
        return new Result<Parking>().getSuccessResult(parking);
    }


    @DeleteMapping("/parking/{id}")
    public Result<Parking> deleteParking(String id,
                                         @PathVariable("id") String parkingId)
            throws NotFoundResourceException {
        parkingService.removeParking(parkingId);
        return new Result<Parking>().getSuccessResult(null);
    }
}
