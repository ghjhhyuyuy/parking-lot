package com.parking.lot.aspect;

import com.parking.lot.entity.Staff;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.RoleType;
import com.parking.lot.exception.NotManagerException;
import com.parking.lot.repository.StaffRepository;
import com.parking.lot.request.Request;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ManagerAspect {

    private final StaffRepository staffRepository;

    @Autowired
    public ManagerAspect(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Pointcut("@annotation(com.parking.lot.annotation.ManagerCheck)")
    private void cut() {

    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        String id = getParamUserId(joinPoint);
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new NotManagerException(ExceptionMessage.NOT_FOUND_STAFF));
        if (!staff.getRole().equals(RoleType.MANGER.getId())) {
            throw new NotManagerException(ExceptionMessage.NOT_MANAGER);
        }
    }

    private String getParamUserId(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        Request request = (Request) objects[0];
        return request.getId();
    }
}
