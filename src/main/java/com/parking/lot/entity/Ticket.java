package com.parking.lot.entity;

import static com.parking.lot.entity.Storage.getStorage;

import com.parking.lot.repository.CarRepository;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.RoleRepository;
import com.parking.lot.repository.StorageRepository;
import com.parking.lot.repository.TicketRepository;
import com.parking.lot.repository.UserRepository;
import com.parking.lot.util.GenerateID;
import com.parking.lot.util.TimeUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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
        .id(GenerateID.getUUID())
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
