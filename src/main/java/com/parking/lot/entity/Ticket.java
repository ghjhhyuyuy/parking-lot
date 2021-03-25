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

  @Id
  private String id;
  private String entryTime;
  private String parkingLotId;
  private String storageId;

}
