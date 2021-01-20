package com.parking.lot.handler;

import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.MyException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.NotPartingHelperException;
import com.parking.lot.exception.OverSizeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({OverSizeException.class, IllegalTicketException.class,
      NotFoundResourceException.class,
      NotPartingHelperException.class})
  @ResponseBody
  public Result<Object> overSizeExceptionHandler(MyException e) {
    return Result.getExceptionResult(e.getExceptionMessage().getCode(),
        e.getExceptionMessage().getMessage());
  }
}
