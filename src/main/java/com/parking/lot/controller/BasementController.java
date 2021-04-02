package com.parking.lot.controller;

import com.parking.lot.annotation.ManagerCheck;
import com.parking.lot.entity.Basement;
import com.parking.lot.entity.Car;
import com.parking.lot.entity.Ticket;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.request.AddBasementRequest;
import com.parking.lot.request.Request;
import com.parking.lot.service.BasementService;
import com.parking.lot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basement")
public class BasementController {

    private final BasementService basementService;

    @Autowired
    public BasementController(
            BasementService basementService) {
        this.basementService = basementService;
    }

    @PostMapping("/self/parkingCar/{id}")
    public Result<Ticket> parkingCarBySelf(@PathVariable("id") String basementId, @RequestBody Car car)
            throws NotFoundResourceException {
        Ticket ticket = basementService.parkingCarBySelf(basementId, car);
        return new Result<Ticket>().getSuccessResult(ticket);
    }

    @PostMapping("/staff/parkingCar/{id}")
    public Result<Ticket> parkingCarByStaff(@PathVariable("id") String userId,
                                            @RequestBody Car car)
            throws NotFoundResourceException {
        Ticket ticket = basementService.parkingCarByStaff(userId, car);
        return new Result<Ticket>().getSuccessResult(ticket);
    }

    @GetMapping("/takeCar/{id}")
    public Result<Car> takeCar(@PathVariable("id") String ticketId, String willTakeCarId)
            throws NotFoundResourceException {
        Car canTake = basementService.takeCar(ticketId, willTakeCarId);
        return new Result<Car>().getSuccessResult(canTake);
    }

    @PostMapping
    @ManagerCheck
    public Result<Basement> addBasement(@RequestBody AddBasementRequest addBasementRequest)
            throws NotFoundResourceException {
        Basement basement = basementService.addBasement(addBasementRequest.getSize());
        return new Result<Basement>().getSuccessResult(basement);
    }


    @DeleteMapping("/{id}")
    @ManagerCheck
    public Result<Basement> deleteBasement(Request request,
                                           @PathVariable("id") String basementId)
            throws NotFoundResourceException {
        basementService.removeBasement(basementId);
        return new Result<Basement>().getSuccessResult(null);
    }
}
