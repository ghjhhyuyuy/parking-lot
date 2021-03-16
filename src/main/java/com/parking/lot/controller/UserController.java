package com.parking.lot.controller;

import com.parking.lot.entity.User;
import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(
            UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Result<User> addUser(String id, String name, String role)
            throws NotFoundResourceException {
        User user = userService.addUser(name, role);
        return new Result<User>().getSuccessResult(user);
    }

    @DeleteMapping("/{id}")
    public Result<User> deleteUser(@PathVariable("id") String userId)
            throws NotFoundResourceException {
        User user = userService.removeUser(userId);
        return new Result<User>().getSuccessResult(user);
    }
}
