package com.parking.lot.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
  String code;
  String message;
  T data;

  public static Result<Object> getExceptionResult(String code, String message) {
    return new Result<>(code,message,null);
  }

  public Result<T> getSuccessResult(T data) {
    return new Result<>("200","success",data);
  }
}
