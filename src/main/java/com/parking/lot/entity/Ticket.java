package com.parking.lot.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

  final static int ticket_time = 3600000;
  String id;
  String timeoutDate;
  String parkingLotId;

  public static Ticket getTicket(String parkingLotId) {
    Date date = new Date(System.currentTimeMillis() + ticket_time);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return Ticket.builder()
        .id(UUID.randomUUID().toString())
        .parkingLotId(parkingLotId)
        .timeoutDate(simpleDateFormat.format(date))
        .build();
  }

  public boolean checkTicket() throws ParseException {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date timeoutDate = simpleDateFormat.parse(this.timeoutDate);
    return date.before(timeoutDate);
  }
}
