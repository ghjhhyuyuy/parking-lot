package com.parking.lot.handler;

import com.parking.lot.exception.*;
import com.parking.lot.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OverSizeException.class, IllegalTicketException.class,
            NotFoundResourceException.class, NotManagerException.class, IllegalSizeException.class,
            NotRightCarException.class, NotParkingStaffException.class, OutOfSetException.class,
            NoMatchingRoleException.class, StillCarInBasementException.class})
    @ResponseBody
    public Result<Object> overSizeExceptionHandler(MyException e) {
        return Result.getExceptionResult(e.getExceptionMessage().getCode(),
                e.getExceptionMessage().getMessage());
    }
}
