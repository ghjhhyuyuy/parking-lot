package com.parking.lot.handler;

import com.parking.lot.vo.Result;
import com.parking.lot.exception.IllegalSizeException;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.MyException;
import com.parking.lot.exception.NoMatchingRoleException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.NotManagerUserException;
import com.parking.lot.exception.NotParkingHelperException;
import com.parking.lot.exception.NotRightCarException;
import com.parking.lot.exception.OutOfSetException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.exception.StillCarInParkingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({OverSizeException.class, IllegalTicketException.class,
      NotFoundResourceException.class, NotManagerUserException.class, IllegalSizeException.class,
      NotRightCarException.class, NotParkingHelperException.class, OutOfSetException.class,
      NoMatchingRoleException.class, StillCarInParkingException.class})
  @ResponseBody
  public Result<Object> overSizeExceptionHandler(MyException e) {
    return Result.getExceptionResult(e.getExceptionMessage().getCode(),
        e.getExceptionMessage().getMessage());
  }
}
