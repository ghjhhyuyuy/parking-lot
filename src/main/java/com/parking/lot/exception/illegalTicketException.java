package com.parking.lot.exception;

import com.parking.lot.enums.ExceptionMessage;

public class illegalTicketException extends Throwable {

  public illegalTicketException() {
    super(ExceptionMessage.ILLEGAL_TICKET.getMessage());
  }
}
