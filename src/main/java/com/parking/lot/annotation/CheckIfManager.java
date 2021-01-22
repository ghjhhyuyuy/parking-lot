package com.parking.lot.annotation;

import com.parking.lot.entity.User;
import com.parking.lot.enums.RoleType;
import com.parking.lot.repository.UserRepository;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Optional;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

@Target(ElementType.PARAMETER)
@Constraint(validatedBy = CheckIfManager.ManagerChecker.class)
public @interface CheckIfManager {

  class ManagerChecker implements ConstraintValidator<CheckIfManager, String> {

    private final UserRepository userRepository;

    @Autowired
    public ManagerChecker(UserRepository userRepository) {
      this.userRepository = userRepository;
    }

    @Override
    public void initialize(CheckIfManager constraintAnnotation) {

    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
      Optional<User> optionalUser = userRepository.findById(id);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        return user.getRole().equals(RoleType.MANGER.getId());
      }
      return false;
    }
  }
}
