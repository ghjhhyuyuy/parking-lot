package com.parking.lot.entity;

import com.parking.lot.util.GenerateId;
import com.parking.lot.util.TimeUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd HH:mm:ss");
  @Id
  private String id;
  private String timeoutDate;
  private String parkingLotId;
  private String storageId;

  public static Ticket createTicket(Parking parking, Storage storage) {
    return Ticket.builder()
        .id(GenerateId.getUUID())
        .parkingLotId(parking.getId())
        .timeoutDate(dateTimeFormatter.format(TimeUtil.getTime(ticket_time_hour)))
        .storageId(storage.getId())
        .build();
  }

  public boolean checkTicket() {
    LocalDateTime localDateTime = TimeUtil.getTime(0);
    LocalDateTime timeoutDate = LocalDateTime.parse(this.timeoutDate, dateTimeFormatter);
    return localDateTime.isBefore(timeoutDate);
  }
}
