package com.parking.lot.entity;

import com.parking.lot.util.GenerateID;
import com.parking.lot.util.TimeUtil;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

  final static int ticket_time_hour = 1;
  @Id
  private String id;
  private String timeoutDate;
  private String parkingLotId;

  public static Ticket getTicket(String parkingLotId) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return Ticket.builder()
        .id(GenerateID.getUUID())
        .parkingLotId(parkingLotId)
        .timeoutDate(simpleDateFormat.format(TimeUtil.getTime(ticket_time_hour)))
        .build();
  }

  public boolean checkTicket(){
    LocalDateTime localDateTime = TimeUtil.getTime(0);
    LocalDateTime timeoutDate = LocalDateTime.parse(this.timeoutDate);
    return localDateTime.isBefore(timeoutDate);
  }
}
