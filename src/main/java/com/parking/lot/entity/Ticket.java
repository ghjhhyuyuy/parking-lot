package com.parking.lot.entity;

import com.parking.lot.util.GenerateID;
import com.parking.lot.util.TimeUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

  final static int ticket_time = 3600000;
  @Id
  private String id;
  private String timeoutDate;
  private String parkingLotId;

  public static Ticket getTicket(String parkingLotId) {
    return Ticket.builder()
        .id(GenerateID.getUUID())
        .parkingLotId(parkingLotId)
        .timeoutDate(TimeUtil.getTime(ticket_time))
        .build();
  }

  public boolean checkTicket() throws ParseException {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date timeoutDate = simpleDateFormat.parse(this.timeoutDate);
    return date.before(timeoutDate);
  }
}
