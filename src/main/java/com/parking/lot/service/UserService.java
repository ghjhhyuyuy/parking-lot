package com.parking.lot.service;

import com.parking.lot.entity.User;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.repository.UserRepository;
import com.parking.lot.util.GenerateId;
import com.parking.lot.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class UserService {
    private final UserRepository userRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(String name, String role) {
        User user = new User(GenerateId.getUUID(), name, role, dateTimeFormatter.format(TimeUtil.getTime(0)),
                null);
        return userRepository.save(user);
    }

    public User removeUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER));
        user = remove(user);
        return userRepository.save(user);
    }

    private User remove(User user) {
        return User.builder().id(user.getId()).name(user.getName()).createDate(user.getCreateDate())
                .role(
                        user.getRole()).removeDate(dateTimeFormatter.format(TimeUtil.getTime(0))).build();
    }
}
