package com.parking.lot.handler;

import com.parking.lot.entity.vo.Result;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.OverSizeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundResourceException.class)
  @ResponseBody
  public Result<Object> notFoundResourceExceptionHandler(NotFoundResourceException e) {
    return Result.getExceptionResult(e.getExceptionMessage().getCode(), e.getExceptionMessage().getMessage());
  }

  @ExceptionHandler(IllegalTicketException.class)
  @ResponseBody
  public Result<Object> illegalTicketExceptionHandler(IllegalTicketException e) {
    return Result.getExceptionResult(e.getExceptionMessage().getCode(), e.getExceptionMessage().getMessage());
  }

  @ExceptionHandler(OverSizeException.class)
  @ResponseBody
  public Result<Object> overSizeExceptionHandler(OverSizeException e) {
    return Result.getExceptionResult(e.getExceptionMessage().getCode(), e.getExceptionMessage().getMessage());
  }
}
