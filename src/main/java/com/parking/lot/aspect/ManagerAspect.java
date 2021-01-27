package com.parking.lot.aspect;

import com.parking.lot.entity.User;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.RoleType;
import com.parking.lot.exception.NotManagerUserException;
import com.parking.lot.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ManagerAspect {

  private final UserRepository userRepository;

  @Autowired
  public ManagerAspect(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Before("execution(public * com.parking.lot.controller.ManagerController.*(..))")
  public void before(JoinPoint joinPoint) {
    Object[] objects = joinPoint.getArgs();
    String id = (String) objects[0];
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotManagerUserException(ExceptionMessage.NOT_FOUND_USER));
    if (!user.getRole().equals(RoleType.MANGER.getId())) {
      throw new NotManagerUserException(ExceptionMessage.NOT_MANAGER_USER);
    }
  }
}
