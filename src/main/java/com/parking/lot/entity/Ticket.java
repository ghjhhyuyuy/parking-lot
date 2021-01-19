package com.parking.lot.entity;

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
  String id;
  String timeoutDate;
  String parkingLotId;

  final static int ticket_time = 3600000;

  public static Ticket getTicket() {
    Date date = new Date(System.currentTimeMillis() + ticket_time);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return Ticket.builder()
        .id(UUID.randomUUID().toString())
        .parkingLotId(UUID.randomUUID().toString())
        .timeoutDate(simpleDateFormat.format(date))
        .build();
  }
}
