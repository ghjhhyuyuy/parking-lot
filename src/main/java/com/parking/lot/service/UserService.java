package com.parking.lot.service;

import com.parking.lot.entity.User;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User addUser(String name, String role) {
        User user = User.createUser(name, role);
        return userRepository.save(user);
    }

    public User removeUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER));
        user = User.removeUser(user);
        return userRepository.save(user);
    }
}
