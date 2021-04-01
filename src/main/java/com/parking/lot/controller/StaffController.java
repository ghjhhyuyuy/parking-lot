package com.parking.lot.controller;

import com.parking.lot.entity.Staff;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.request.AddStaffRequest;
import com.parking.lot.service.StaffService;
import com.parking.lot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(
            StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public Result<Staff> addStaff(@RequestBody AddStaffRequest addStaffRequest)
            throws NotFoundResourceException {
        Staff staff = staffService.addStaff(addStaffRequest.getName(), addStaffRequest.getRole());
        return new Result<Staff>().getSuccessResult(staff);
    }

    @DeleteMapping("/{id}")
    public Result<Staff> deleteStaff(String id, @PathVariable("id") String userId)
            throws NotFoundResourceException {
        Staff staff = staffService.removeStaff(userId);
        return new Result<Staff>().getSuccessResult(staff);
    }
}
